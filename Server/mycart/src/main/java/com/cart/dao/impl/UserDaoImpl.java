/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cart.dao.impl;

import com.cart.config.JwtTokenUtil;
import com.cart.controller.UserController;
import com.cart.dao.intf.UserDaoIntf;
import com.cart.pojo.CartItems;
import com.cart.pojo.DeliveryAddress;
import com.cart.pojo.DeliveryCharge;
import com.cart.pojo.GeneralSetting;
import com.cart.pojo.Seller;
import com.cart.pojo.SellerStockMap;
import com.cart.pojo.SellerUserMap;
import com.cart.pojo.SubcriptionDetails;
import com.cart.pojo.SubscriptionPlans;
import com.cart.pojo.UserLatestInfo;
import com.cart.pojo.Users;
import com.cart.util.AESEncryption;
import com.cart.util.ConvertJSONtoObject;
import com.cart.util.GeneralUtil;
import com.cart.util.SendEmail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Shrehan
 */
@Repository
public class UserDaoImpl implements UserDaoIntf {

	private static final Logger logger = LogManager.getLogger(UserController.class);
	@Autowired
	private EntityManager entityManager;

	@Autowired
	private TransactionsDaoImpl transactionsDaoImpl;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public JSONObject loginValidation(String mobileNo, String password, JSONObject json) {
		JSONObject result = new JSONObject();

		try {
			AESEncryption.init();
			logger.info(password);
			String pas = AESEncryption.getInstance().encode(password);
			String sql = "FROM Users a where (a.mobileNo=:login_name or a.email=:login_name ) and a.password='" + pas
					+ "'";
			logger.info(sql);
			List<Users> ls = entityManager.createQuery(sql).setParameter("login_name", mobileNo).getResultList();
			if (!ls.isEmpty()) {
				Users u = ls.get(0);
				u.setLastLogin(new Date());
				result = getUserJson(u, u.getMobileNo());
				result.put("password", password);
				result.put("days_exp", 100);
				result.put("cart_count", 0);
				result.put("sub_id", 0);
				if (!u.getUserType().equalsIgnoreCase("Personal")) {
					result.put("iscons", false);
					result.put("seller_id", 0);
					result.put("noseller", true);
					if (u.getUserType().equalsIgnoreCase("seller")) {
						Seller s = getSellerDetails(u.getUserId());
						if (s != null) {
							result.put("seller_id", s.getSellerId());
							result.put("noseller", false);
							result.put("sub_id", s.getSubscriptionId());
							if (s.getExpiredDate() != null) {
								result.put("exp_date", GeneralUtil.dateToString(s.getExpiredDate()));
								long days = GeneralUtil.daysBetweenFromCurrentDate(s.getExpiredDate());
								SubcriptionDetails sd = getSubcriptionDetails(0, s.getSellerId(), "pending");
								result.put("cur_days", days);
								days += (sd != null ? sd.getDurationDays() : 0);
								result.put("days_exp", days);
							} else {
								result.put("days_exp", -1);
							}

						}
					} else {
						result.put("noseller", false);
					}
					result.put("delivery_type", u.getDeliveryType());

				} else {

					result.put("seller_id", 0);
					result.put("noseller", false);
					result.put("iscons", true);
					DeliveryAddress d = getDeliveryAddress(u.getUserId(), json);
					if (d != null) {
						result.put("isdelivery", true);
						result.put("delivery_add_id", d.getDelveryAddressId());
						result.put("address",
								d.getAddress() + ", " + d.getCity() + ", " + d.getState() + ", " + d.getPinCode());
						result.put("latitude", d.getLatitude());
						result.put("longitude", d.getLongitude());
						List<CartItems> cits = transactionsDaoImpl.getCartItems(u.getUserId(), "active", 0,
								new JSONObject(), 0, d.getDelveryAddressId());
						result.put("cart_count", cits.size());

					} else {
						result.put("isdelivery", false);
					}
				}

				result.put("delivery_type", u.getDeliveryType() == null ? "no" : u.getDeliveryType());
				if (u.getDeliveryType() != null) {
					UserLatestInfo a = getUserLatestInfo(u.getUserId(), true);
					result.put("latest_info_id", a != null ? a.getLastNotificationId() : 0);
				}
				String token = generateToken(u.getMobileNo(), password, false);
				logger.info("token : " + token);
				result.put("token", token);
				entityManager.persist(u);
			} else {
				result.put("status", "Invalid Mobile No./Email or  Password");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "Invalid Mobile No./Email or  Password");
		}
		logger.info(result.toString());
		return result;
	}

	public String generateToken(String mobileNo, String password, boolean validation) {
		if (!validation)
			return jwtTokenUtil.generateToken(new User(mobileNo, bcryptEncoder.encode(password), new ArrayList<>()));
		else {
			try {
				AESEncryption.init();
				logger.info(password);
				String pas = AESEncryption.getInstance().encode(password);
				String sql = "FROM Users a where (a.mobileNo=:login_name or a.email=:login_name ) and a.password='"
						+ pas + "'";
				logger.info(sql);
				List<Users> ls = entityManager.createQuery(sql).setParameter("login_name", mobileNo).getResultList();
				if (!ls.isEmpty()) {
					return jwtTokenUtil.generateToken(
							new User(ls.get(0).getMobileNo(), bcryptEncoder.encode(password), new ArrayList<>()));
				} else {
					return "invalid";
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "invalid";
			}
		}
	}

	public UserLatestInfo getUserLatestInfo(long userId, boolean active) {
		try {
			String sql = "From UserLatestInfo a where a.userId=" + userId;
			List<UserLatestInfo> ls = entityManager.createQuery(sql).getResultList();
			if (!ls.isEmpty()) {
				if (active) {
					UserLatestInfo ul = ls.get(0);
					ul.setIsActive(1);
					entityManager.merge(ul);
				}
				return ls.get(0);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public DeliveryAddress getDeliveryAddress(long userId, JSONObject json) {
		DeliveryAddress d = null;
		try {
			String sql = "From DeliveryAddress a where a.userId=" + userId + " and a.defaultDel=true ";
			List<DeliveryAddress> ls = entityManager.createQuery(sql).getResultList();
			if (!ls.isEmpty())
				d = ls.get(0);
			else {
				sql = "From DeliveryAddress a where a.userId=" + userId;
				ls = entityManager.createQuery(sql).getResultList();
				if (!ls.isEmpty())
					d = ls.get(0);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return d;
	}

	public JSONObject changesPassword(long userId, String oldpassword, String newPassword) {
		JSONObject result = new JSONObject();
		String reason = "";
		try {
			AESEncryption.init();
			String pas = AESEncryption.getInstance().encode(oldpassword);
			String sql = "FROM Users a where a.userId=" + userId + " and a.password='" + pas + "'";
			List<Users> ls = entityManager.createQuery(sql).getResultList();
			if (!ls.isEmpty()) {
				Users u = ls.get(0);
				u.setPassword(AESEncryption.getInstance().encode(newPassword));
				u.setFirstLogin(1);
				u.setUpdatedOn(new Date());
				entityManager.merge(u);
				reason = "success";
			} else {
				reason = "User not exists";
			}
		} catch (Exception e) {
			e.printStackTrace();
			reason = "User not exists";
		}
		result.put("reason", reason);
		return result;
	}

	public Seller getSellerDetails(long userId) {
		Seller seller = null;
		try {
			String sql = "FROM Seller a where a.sellerId=(select b.sellerId from SellerUserMap b where b.userId= "
					+ userId + ")";
			List<Seller> ls = entityManager.createQuery(sql).getResultList();
			if (!ls.isEmpty()) {
				seller = ls.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return seller;
	}

	public Seller getSeller(long sellerId) {
		Seller seller = null;
		try {
			String sql = "FROM Seller a where a.sellerId= " + sellerId + "";
			List<Seller> ls = entityManager.createQuery(sql).getResultList();
			if (!ls.isEmpty()) {
				seller = ls.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return seller;
	}

	public JSONObject saveOrUpdateSellerDetails(JSONObject json) {
		JSONObject result = new JSONObject();
		String reason = "";
		try {
			long userId = GeneralUtil.objTolong(json.get("user_id"));
			Seller s = getSellerDetails(userId);
			if (s == null) {
				s = new Seller();
				ConvertJSONtoObject.jsonToObject(s, json);
				s.setSellerId(GeneralUtil.getPrimaryKey("SEQ_USERS", entityManager));
				s.setUserId(userId);
				if (((JSONObject) json.get("image")).get("value") != null
						&& ((JSONObject) json.get("image")).get("value").toString().length() > 0) {
					String image = GeneralUtil.writeImageFolder(
							((JSONObject) json.get("image")).get("value").toString(), "mycart/seller/",
							s.getSellerId() + "");
					s.setImage(image);
				}
				s.setCreatedOn(new Date());
				saveSellerUserMap(userId, s.getSellerId());
				entityManager.persist(s);
				result.put("reason", "Added successfully");
				result.put("status", "success");
			} else {
				ConvertJSONtoObject.jsonToObject(s, json);
				if (((JSONObject) json.get("image")).get("value") != null
						&& ((JSONObject) json.get("image")).get("value").toString().length() > 0) {
					String image = GeneralUtil.writeImageFolder(
							((JSONObject) json.get("image")).get("value").toString(), "mycart/seller/",
							s.getSellerId() + "");
					s.setImage(image);
				}
				s.setUpdatedOn(new Date());
				s.setStatus("active");
				entityManager.persist(s);

				result.put("reason", "Updated successfully");
				result.put("status", "success");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("reason", "Data Saved Failed");
			result.put("status", "failed");
		}
		return result;
	}

	public JSONObject getUserJson(Users u, String mobileNo) {
		JSONObject result = new JSONObject();
		try {
			if (u == null) {
				u = getUserByMoblleNo(mobileNo);
			}
			result = ConvertJSONtoObject.pojoToJSON(u);
			result.put("name", u.getFirstName() + " " + u.getLastName());
			result.put("sync_status", "closed");
			result.put("isoffline", u.getIsOffline() == 1 ? true : false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info(result.toString());
		return result;
	}

	@Override
	public String saveUser(JSONObject json) {
		String result = "";
		try {
			Users user = new Users();
			ConvertJSONtoObject.jsonToObject(user, json);
			Users u = getUserByMoblleNo(user.getMobileNo());

			if (u == null) {
				u = getUserByEmail(user.getEmail());

				if (u == null) {
					user.setUserId(GeneralUtil.getPrimaryKey("SEQ_USERS", entityManager));
					AESEncryption.init();
					long rma = 0;
					if (user.getPassword() == null) {
						rma = GeneralUtil.otpGenerate(1000, 9999);
						user.setPassword(rma + "");
					}
					String pas = AESEncryption.getInstance().encode(user.getPassword());
					user.setPassword(pas);

					user.setCreatedBy(user.getFirstName() + " " + user.getLastName());
					if (user.getUserType().equalsIgnoreCase("personal")
							|| user.getUserType().equalsIgnoreCase("retailer")) {
						user.setIsOffline(1);
					} else if (json.containsKey("seller_id") && user.getUserType().equalsIgnoreCase("seller")) {
						saveSellerUserMap(user.getUserId(),
								GeneralUtil.objTolong(((JSONObject) json.get("seller_id")).get("value")));
					}
					user.setCreatedOn(new Date());
					user.setStatus("active");
					if (json.containsKey("image")) {
						if (((JSONObject) json.get("image")).get("value") != null
								&& ((JSONObject) json.get("image")).get("value").toString().length() > 0) {
							String image = GeneralUtil.writeImageFolder(
									((JSONObject) json.get("image")).get("value").toString(), "mycart/users/",
									user.getMobileNo());
							user.setImage(image);
						}
					}
					if (rma > 0) {
						SendEmail.sendMail(user.getEmail(), "Password", "Dear " + user.getFirstName()
								+ ", \n \n \n Please use  " + rma + " Password for login");
					}
					entityManager.persist(user);
					result = "success";
				} else {
					result = "Email already registered";
				}
			} else {
				result = "Mobile number already registered";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONObject forgotPassword(JSONObject json) {
		JSONObject result = new JSONObject();
		try {
			String email = GeneralUtil.objToString(json.get("login_id"));
			Users u = getUserByEmail(email);
			u = u == null ? getUserByMoblleNo(email) : u;
			if (u != null) {
				AESEncryption.init();
				String pas = AESEncryption.getInstance().decode(u.getPassword());
				if (pas != null) {
					String res = SendEmail.sendMail(u.getEmail(), "Password",
							"Dear " + u.getFirstName() + ", \n \n \n Please use  " + pas + " Password for login");
					if (res.equalsIgnoreCase("success")) {
						u.setFirstLogin(0);
						entityManager.merge(u);
						result.put("reason", "success");

					} else {
						result.put("reason", "Unable to send mail");
					}
				}
			} else {
				result.put("reason", "Invalid details");
			}

		} catch (Exception e) {

		}
		return result;
	}

	public void saveSellerUserMap(long userId, long sellerId) {
		SellerUserMap sum = new SellerUserMap();
		sum.setSellerUserMapId(GeneralUtil.getPrimaryKey("SEQ_SELLER_USER_MAP", entityManager));
		sum.setSellerId(sellerId);
		sum.setUserId(userId);
		entityManager.persist(sum);
	}

	@Override
	public JSONObject updateUser(JSONObject json) {
		JSONObject rejson = new JSONObject();
		String result = "";
		try {
			Users temp = new Users();
			ConvertJSONtoObject.jsonToObject(temp, json);
			if (json.containsKey("user_id")) {
				temp.setUserId(GeneralUtil.objTolong(((JSONObject) json.get("user_id")).get("value")));
			}
			Users user = entityManager.find(Users.class, temp.getUserId());
			Users u = null;
			if (!temp.getMobileNo().equalsIgnoreCase(user.getMobileNo())) {
				u = getUserByMoblleNo(temp.getMobileNo(), user.getUserId());

			}
			if (u == null) {
				if (!temp.getEmail().equalsIgnoreCase(user.getEmail())) {
					u = getUserByEmail(temp.getMobileNo(), user.getUserId());
				}
				if (u == null) {
					if (temp.getFirstName() != null && temp.getFirstName().length() > 0) {
						user.setFirstName(temp.getFirstName());
					}
					if (temp.getLastName() != null && temp.getLastName().length() > 0) {
						user.setLastName(temp.getLastName());
					}
					user.setMobileNo(temp.getMobileNo());
					user.setEmail(temp.getEmail());
					user.setUpdatedBy(temp.getUpdatedBy());
					user.setUpdatedOn(temp.getUpdatedOn());
					if (((JSONObject) json.get("image")).get("value") != null
							&& ((JSONObject) json.get("image")).get("value").toString().length() > 0) {
						String image = GeneralUtil.writeImageFolder(
								((JSONObject) json.get("image")).get("value").toString(), "mycart/users/",
								user.getMobileNo());
						user.setImage(image);
					}
					entityManager.merge(user);
					rejson = getUserJson(user, "");
					result = "success";
				} else {
					result = "Email already registered";
				}
			} else {
				result = "Mobile number already registered";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		rejson.put("reason", result);
		return rejson;
	}

	public Users getUserByMoblleNo(String mobleNo) {
		Users user = null;
		try {
			String sql = "FROM Users a where a.mobileNo='" + mobleNo + "'";
			List<Users> ls = entityManager.createQuery(sql).getResultList();
			if (!ls.isEmpty()) {
				user = ls.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	public Users getUserByMoblleNo(String mobleNo, long userId) {
		Users user = null;
		try {
			String sql = "FROM Users a where a.mobileNo='" + mobleNo + "' and a.userId!=" + userId;
			List<Users> ls = entityManager.createQuery(sql).getResultList();
			if (!ls.isEmpty()) {
				user = ls.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	public Users getUserByEmail(String email) {
		Users user = null;
		try {
			String sql = "FROM Users a where a.email='" + email + "'";
			List<Users> ls = entityManager.createQuery(sql).getResultList();
			if (!ls.isEmpty()) {
				user = ls.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	public Users getUserByEmail(String email, long userId) {
		Users user = null;
		try {
			String sql = "FROM Users a where a.email='" + email + "' and a.userId!=" + userId;
			List<Users> ls = entityManager.createQuery(sql).getResultList();
			if (!ls.isEmpty()) {
				user = ls.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public JSONObject addDeliveryAddress(JSONObject json) {
		JSONObject rejson = new JSONObject();
		String result = "";
		try {
			DeliveryAddress delAdd = new DeliveryAddress();
			ConvertJSONtoObject.jsonToObject(delAdd, json);
			List<DeliveryAddress> u = getDeliveryAddress(delAdd.getUserId(), 0, delAdd.getAddress(), null);
			if (u.isEmpty()) {
				delAdd.setDelveryAddressId(GeneralUtil.getPrimaryKey("SEQ_DELIVERY_ADDRESS", entityManager));
				delAdd.setStatus("active");
				if (delAdd.isDefaultDel()) {
					String sql = "update delivery_address a set a.is_default=0 where a.user_id=" + delAdd.getUserId();
					entityManager.createNativeQuery(sql).executeUpdate();
				}
				entityManager.persist(delAdd);
				result = "success";
				rejson = ConvertJSONtoObject.pojoToJSON(delAdd);
			} else {
				result = "Delivery address already registered";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		rejson.put("reason", result);
		return rejson;
	}

	@Override
	public JSONObject updateDeliveryAddress(DeliveryAddress delAdd) {
		JSONObject rejson = new JSONObject();
		String result = "";
		try {
			List<DeliveryAddress> u = getDeliveryAddress(delAdd.getUserId(), delAdd.getDelveryAddressId(), null, null);
			if (!u.isEmpty()) {

				DeliveryAddress d = u.get(0);
				logger.info("delAdd : " + delAdd.isDefaultDel());
				if (delAdd.isDefaultDel() && !d.isDefaultDel()) {
					String sql = "update delivery_address a set a.is_default=0 where a.user_id=" + delAdd.getUserId()
							+ " and a.delivery_add_id!=" + delAdd.getDelveryAddressId();
					entityManager.createNativeQuery(sql).executeUpdate();
				}
				d.setUpdatedOn(new Date());
				d.setAddress(delAdd.getAddress());
				d.setState(delAdd.getState());
				d.setCity(delAdd.getCity());
				d.setPinCode(delAdd.getPinCode());
				d.setLatitude(delAdd.getLatitude());
				d.setLongitude(delAdd.getLongitude());
				d.setUpdatedBy(delAdd.getUpdatedBy());
				d.setUpdatedOn(delAdd.getUpdatedOn());
				d.setDefaultDel(delAdd.isDefaultDel());
				entityManager.merge(d);
				result = "success";
				rejson = ConvertJSONtoObject.pojoToJSON(d);
			} else {
				result = "Delivery address already registered";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		rejson.put("reason", result);
		return rejson;
	}

	public List<DeliveryAddress> getDeliveryAddress(long useerId, long id, String address, Date date) {
		List<DeliveryAddress> obj = new ArrayList<>();
		try {
			String sql = "FROM DeliveryAddress a where a.userId=" + useerId;
			if (id > 0) {
				sql += " and a.delveryAddressId=" + id;
			}
			if (address != null && address.length() > 0) {
				sql += " and a.address=:address";
			}
			if (date != null) {
				sql += " and (a.createdOn>:syncdate or a.updatedOn>:syncdate)";
			}
			sql += " order by a.address ";
			Query q = entityManager.createQuery(sql);
			if (address != null && address.length() > 0) {
				q.setParameter("address", address);
			}
			if (date != null) {
				q.setParameter("syncdate", date);
			}
			obj = q.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public JSONArray getSellerMappedItems(JSONObject json) {
		JSONArray items = new JSONArray();
		try {
			String sql = " select m.SELLER_STOCK_MAP_ID,a.stock_item_id,a.name,b.name cat_name,c.name sub_cat_name,d.name bar_name,a.image,a.code  "
					+ " from stock_items a,seller_stock_map m,category b,sub_category c,brands d where a.status='active' "
					+ " and a.stock_item_id = m.stock_item_id " + " and a.category_id=b.category_id "
					+ " and a.sub_cat_id=c.sub_cat_id " + " and a.brand_id=d.brand_id(+)";

			if (GeneralUtil.objTolong(json.get("seller_id")) > 0) {
				sql += " and m.seller_id = " + GeneralUtil.objTolong(json.get("seller_id"));
			}
			if (GeneralUtil.objTolong(json.get("categoryId")) > 0) {
				sql += " and a.category_id=" + GeneralUtil.objTolong(json.get("categoryId"));
			}
			if (GeneralUtil.objTolong(json.get("subCategoryId")) > 0) {
				sql += " and a.sub_cat_id=" + GeneralUtil.objTolong(json.get("subCategoryId"));
			}
			if (GeneralUtil.objTolong(json.get("brandId")) > 0) {
				sql += " and a.brand_id=" + GeneralUtil.objTolong(json.get("brandId"));
			}
			if (json.containsKey("name") && json.get("name") != null && json.get("name").toString().length() > 0) {
				sql += " and (lower(a.name) like '%'||:sku_name||'%' or lower(a.description) like '%'||:sku_name||'%') ";
			}
			sql += " order by lower(a.name) ";
			Query q = entityManager.createNativeQuery(sql);
			if (json.containsKey("name") && json.get("name") != null && json.get("name").toString().length() > 0) {
				q.setParameter("sku_name", json.get("name").toString().toLowerCase());
			}
			int pageFirst = 0;
			int pagesize = 0;
			if (json.containsKey("first")) {
				pageFirst = Integer.parseInt(json.get("first").toString());
			}
			if (json.containsKey("size")) {
				pagesize = Integer.parseInt(json.get("size").toString());
			}
			if (pagesize > 0) {
				q.setFirstResult(pageFirst).setMaxResults(pagesize);
			}
			JSONArray columns = GeneralUtil.getQueryColumns(sql, entityManager, null);
			List<Object[]> ls = q.getResultList();
			items = GeneralUtil.resultToJSONArray(columns, ls);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	public JSONObject mapStockItems(JSONObject json) {
		JSONObject rejson = new JSONObject();
		String result = "";
		try {
			JSONArray items = (JSONArray) json.get("name");
			boolean check = true;
			long sellerId = GeneralUtil.objTolong(json.get("seller_id"));
			if (items == null || items.isEmpty()) {
				check = false;
				items = getStockItems(json);
			}
			for (int i = 0; i < items.size(); i++) {
				long stock_id = GeneralUtil.objTolong(((JSONObject) items.get(i)).get("key"));
				List<Object> ls = new ArrayList<>();
				if (check) {
					ls = entityManager
							.createNativeQuery("select m.stock_item_id from seller_stock_map m where m.seller_id="
									+ sellerId + " and m.stock_item_id=" + stock_id)
							.getResultList();
				}
				if (ls.isEmpty()) {
					SellerStockMap s = new SellerStockMap();
					s.setSellerStockMapId(GeneralUtil.getPrimaryKey("SEQ_SELLER_STOCK_MAP", entityManager));
					s.setSellerId(sellerId);
					s.setStockItemId(stock_id);
					s.setQuantity(0);
					entityManager.persist(s);
					String sql = "INSERT INTO mycart.seller_sp_price ( "
							+ "    price,     pack_config_id,     stock_item_id, "
							+ "    seller_stock_map_id,     seller_sp_price_id,    default_pack "
							+ ") select b.price,b.pack_config_id,b.stock_item_id," + s.getSellerStockMapId()
							+ ",seq_seller_sp_price.nextval,b.default_pack from stock_pack_price b where b.stock_item_id="
							+ stock_id;
					entityManager.createNativeQuery(sql).executeUpdate();
				}

			}
			result = "success";

		} catch (Exception e) {
			e.printStackTrace();
			result = "failed";
		}
		rejson.put("reason", result);
		return rejson;
	}

	public JSONArray getStockItems(JSONObject json) {
		JSONArray items = new JSONArray();
		try {
			String sql = "select a.name name,a.stock_item_id key " + "from stock_items a where a.status='active' "
					+ "and a.category_id=" + GeneralUtil.objTolong(json.get("categoryId"));
			if (GeneralUtil.objTolong(json.get("userId")) > 0) {
				sql += " and a.stock_item_id not in (select m.stock_item_id from seller_stock_map m where m.seller_id="
						+ GeneralUtil.objTolong(json.get("userId")) + ") ";
			}
			if (GeneralUtil.objTolong(json.get("subCategoryId")) > 0) {
				sql += " and a.sub_cat_id=" + GeneralUtil.objTolong(json.get("subCategoryId"));
			}
			if (GeneralUtil.objTolong(json.get("brandId")) > 0) {
				sql += " and a.sub_cat_id=" + GeneralUtil.objTolong(json.get("brandId"));
			}
			JSONArray columns = GeneralUtil.getQueryColumns(sql, entityManager, null);
			List<Object[]> ls = entityManager.createNativeQuery(sql).getResultList();
			items = GeneralUtil.resultToJSONArray(columns, ls);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	public JSONObject updatePriceAndQty(JSONObject obj) {
		JSONObject rejson = new JSONObject();
		String result = "";
		try {
			JSONArray a = (JSONArray) obj.get("packSize");
			long sll=GeneralUtil.objTolong(obj.get("seller_stock_map_id"));
			for (int i = 0; i < a.size(); i++) {
				JSONObject o = (JSONObject) a.get(i);
				long spId = GeneralUtil.objTolong(o.get("seller_sp_price_id"));
				String sql = "";
				if (spId > 0) {
					sql = "update seller_sp_price a set a.price=" + GeneralUtil.objToDouble(o.get("price"))
							+ ",a.default_pack=" + GeneralUtil.objTolong(o.get("default_pack"))
							+ " where a.seller_sp_price_id=" + GeneralUtil.objTolong(o.get("seller_sp_price_id"));
					int d=GeneralUtil.objToInt(GeneralUtil.objTolong(o.get("default_pack")));
					if(d==1)
					{
						String dsql="update seller_stock_map a set a.price="+ GeneralUtil.objToDouble(o.get("price"))+" where a.seller_stock_map_id="+sll;
						entityManager.createNativeQuery(dsql).executeUpdate();
					}
				} else {
					spId = GeneralUtil.objTolong(o.get("stock_pack_price_id"));
					sql = "update stock_pack_price a set a.price=" + GeneralUtil.objToDouble(o.get("price"))
							+ ",a.default_pack=" + GeneralUtil.objTolong(o.get("default_pack"))
							+ " where a.stock_pack_price_id=" + spId;
					
				}
				entityManager.createNativeQuery(sql).executeUpdate();
			}
			result = "success";
		} catch (Exception e) {
			e.printStackTrace();
			result = "failed";
		}
		rejson.put("reason", result);
		return rejson;
	}

	public JSONObject removeStockMap(JSONObject obj) {
		JSONObject rejson = new JSONObject();
		String result = "";
		try {
			long itemId = GeneralUtil.objTolong(obj.get("seller_stock_map_id"));
			int count = entityManager
					.createNativeQuery("delete from  seller_stock_map a where a.seller_stock_map_id=" + itemId)
					.executeUpdate();
			if (count > 0) {
				result = "success";
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "failed";
		}
		rejson.put("reason", result);
		return rejson;
	}

	public JSONArray getAdminUsers(JSONObject json) {
		JSONArray items = new JSONArray();
		try {
			String sql = "select a.user_id,a.first_name,a.last_name,a.moblie_no,a.email,fun_time(a.last_login) last_login,a.user_type,a.image from users a "
					+ " where a.user_type in ('Admin','Delivery') ";
			if (json.containsKey("moblie_no") && json.get("moblie_no") != null
					&& json.get("moblie_no").toString().length() > 0) {
				sql += " and a.moblie_no like  :moblie_no||'%'";
			}
			if (json.containsKey("email") && json.get("email") != null && json.get("email").toString().length() > 0) {
				sql += " and a.email like  :email||'%'";
			}
			if (json.containsKey("user_type") && json.get("user_type") != null
					&& json.get("user_type").toString().length() > 0) {
				sql += " and a.user_type = '" + json.get("user_type").toString() + "'";
			}
			if (json.containsKey("name") && json.get("name") != null && json.get("name").toString().length() > 0) {
				sql += " and (lower(a.first_name) like '%'||:first_name||'%' or lower(a.first_name) like '%'||:first_name||'%') ";
			}

			if (json.containsKey("delivery_user") && json.get("delivery_user").toString().equalsIgnoreCase("yes"))
				sql += " and a.delivery_type='Admin' ";
			else
				sql += " and a.delivery_type is null ";

			sql += " order by lower(a.first_name) ";
			Query q = entityManager.createNativeQuery(sql);
			if (json.containsKey("name") && json.get("name") != null && json.get("name").toString().length() > 0) {
				q.setParameter("first_name", json.get("name").toString().toLowerCase());
			}
			if (json.containsKey("email") && json.get("email") != null && json.get("email").toString().length() > 0) {
				q.setParameter("email", json.get("email").toString().toLowerCase());
			}
			if (json.containsKey("moblie_no") && json.get("moblie_no") != null
					&& json.get("moblie_no").toString().length() > 0) {
				q.setParameter("moblie_no", json.get("moblie_no").toString().toLowerCase());
			}
			int pageFirst = 0;
			int pagesize = 0;
			if (json.containsKey("first")) {
				pageFirst = Integer.parseInt(json.get("first").toString());
			}
			if (json.containsKey("size")) {
				pagesize = Integer.parseInt(json.get("size").toString());
			}
			if (pagesize > 0) {
				q.setFirstResult(pageFirst).setMaxResults(pagesize);
			}
			JSONArray columns = GeneralUtil.getQueryColumns(sql, entityManager, null);
			List<Object[]> ls = q.getResultList();
			items = GeneralUtil.resultToJSONArray(columns, ls);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	public JSONArray getSellers(JSONObject json) {
		JSONArray items = new JSONArray();
		try {
			String sql = "select a.seller_id,a.shop_name,a.mobile_no,a.address,a.city,a.state,a.pin_code,a.licence_no from sellers a where 1=1 ";
			if (json.containsKey("moblie_no") && json.get("moblie_no") != null
					&& json.get("moblie_no").toString().length() > 0) {
				sql += " and a.mobile_no like  :moblie_no||'%'";
			}
			if (json.containsKey("licence_no") && json.get("licence_no") != null
					&& json.get("licence_no").toString().length() > 0) {
				sql += " and a.licence_no like  :email||'%'";
			}
			if (json.containsKey("shop_name") && json.get("shop_name") != null
					&& json.get("shop_name").toString().length() > 0) {
				sql += " and lower(a.shop_name) like '%'||:shop_name||'%' ";
			}
			sql += " order by lower(a.shop_name) ";
			Query q = entityManager.createNativeQuery(sql);
			if (json.containsKey("shop_name") && json.get("shop_name") != null
					&& json.get("shop_name").toString().length() > 0) {
				q.setParameter("shop_name", json.get("shop_name").toString().toLowerCase());
			}
			if (json.containsKey("licence_no") && json.get("licence_no") != null
					&& json.get("licence_no").toString().length() > 0) {
				q.setParameter("licence_no", json.get("licence_no").toString().toLowerCase());
			}
			if (json.containsKey("moblie_no") && json.get("moblie_no") != null
					&& json.get("moblie_no").toString().length() > 0) {
				q.setParameter("moblie_no", json.get("moblie_no").toString().toLowerCase());
			}
			int pageFirst = 0;
			int pagesize = 0;
			if (json.containsKey("first")) {
				pageFirst = Integer.parseInt(json.get("first").toString());
			}
			if (json.containsKey("size")) {
				pagesize = Integer.parseInt(json.get("size").toString());
			}
			if (pagesize > 0) {
				q.setFirstResult(pageFirst).setMaxResults(pagesize);
			}
			JSONArray columns = GeneralUtil.getQueryColumns(sql, entityManager, null);
			List<Object[]> ls = q.getResultList();
			items = GeneralUtil.resultToJSONArray(columns, ls);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	public JSONArray getConsumers(JSONObject json) {
		JSONArray items = new JSONArray();
		try {
			String sql = "select a.user_id,a.first_name,a.last_name,a.moblie_no,a.email,fun_time(a.last_login) last_login,a.user_type from users a "
					+ " where a.user_type in ('Personal','Retailer') ";
			if (json.containsKey("moblie_no") && json.get("moblie_no") != null
					&& json.get("moblie_no").toString().length() > 0) {
				sql += " and a.moblie_no like  :moblie_no||'%'";
			}
			if (json.containsKey("user_type") && json.get("user_type") != null
					&& json.get("user_type").toString().length() > 0) {
				sql += " and a.user_type =  :user_type ";
			}
			if (json.containsKey("email") && json.get("email") != null && json.get("email").toString().length() > 0) {
				sql += " and a.email like  :email||'%'";
			}
			if (json.containsKey("name") && json.get("name") != null && json.get("name").toString().length() > 0) {
				sql += " and (lower(a.first_name) like '%'||:first_name||'%' or lower(a.last_name) like '%'||:first_name||'%') ";
			}
			sql += " order by lower(a.first_name) ";
			Query q = entityManager.createNativeQuery(sql);
			if (json.containsKey("name") && json.get("name") != null && json.get("name").toString().length() > 0) {
				q.setParameter("first_name", json.get("name").toString().toLowerCase());
			}
			if (json.containsKey("email") && json.get("email") != null && json.get("email").toString().length() > 0) {
				q.setParameter("email", json.get("email").toString().toLowerCase());
			}

			if (json.containsKey("user_type") && json.get("user_type") != null
					&& json.get("user_type").toString().length() > 0) {
				q.setParameter("user_type", json.get("user_type").toString());
			}
			if (json.containsKey("moblie_no") && json.get("moblie_no") != null
					&& json.get("moblie_no").toString().length() > 0) {
				q.setParameter("moblie_no", json.get("moblie_no").toString().toLowerCase());
			}
			int pageFirst = 0;
			int pagesize = 0;
			if (json.containsKey("first")) {
				pageFirst = Integer.parseInt(json.get("first").toString());
			}
			if (json.containsKey("size")) {
				pagesize = Integer.parseInt(json.get("size").toString());
			}
			if (pagesize > 0) {
				q.setFirstResult(pageFirst).setMaxResults(pagesize);
			}
			JSONArray columns = GeneralUtil.getQueryColumns(sql, entityManager, null);
			List<Object[]> ls = q.getResultList();
			items = GeneralUtil.resultToJSONArray(columns, ls);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	public JSONArray getSellerUsers(JSONObject json) {
		JSONArray items = new JSONArray();
		try {
			String sql = "select a.user_id,a.first_name,a.last_name,a.moblie_no,a.email,fun_time(a.last_login) last_login,a.user_type,a.image,b.seller_id from users a,seller_user_map b "
					+ " where a.user_id=b.user_id and b.seller_id=" + GeneralUtil.objTolong(json.get("seller_id"));
			if (json.containsKey("moblie_no") && json.get("moblie_no") != null
					&& json.get("moblie_no").toString().length() > 0) {
				sql += " and a.moblie_no like  :moblie_no||'%'";
			}

			if (json.containsKey("email") && json.get("email") != null && json.get("email").toString().length() > 0) {
				sql += " and a.email like  :email||'%'";
			}
			if (json.containsKey("name") && json.get("name") != null && json.get("name").toString().length() > 0) {
				sql += " and (lower(a.first_name) like '%'||:first_name||'%' or lower(a.last_name) like '%'||:first_name||'%') ";
			}

			if (json.containsKey("delivery_user") && json.get("delivery_user").toString().equalsIgnoreCase("yes"))
				sql += " and a.delivery_type='Seller' ";
			else
				sql += " and a.delivery_type is null ";

			sql += " order by lower(a.first_name) ";
			logger.info("SQL : " + sql);
			Query q = entityManager.createNativeQuery(sql);
			if (json.containsKey("name") && json.get("name") != null && json.get("name").toString().length() > 0) {
				q.setParameter("first_name", json.get("name").toString().toLowerCase());
			}
			if (json.containsKey("email") && json.get("email") != null && json.get("email").toString().length() > 0) {
				q.setParameter("email", json.get("email").toString().toLowerCase());
			}

			if (json.containsKey("moblie_no") && json.get("moblie_no") != null
					&& json.get("moblie_no").toString().length() > 0) {
				q.setParameter("moblie_no", json.get("moblie_no").toString().toLowerCase());
			}
			int pageFirst = 0;
			int pagesize = 0;
			if (json.containsKey("first")) {
				pageFirst = Integer.parseInt(json.get("first").toString());
			}
			if (json.containsKey("size")) {
				pagesize = Integer.parseInt(json.get("size").toString());
			}
			if (pagesize > 0) {
				q.setFirstResult(pageFirst).setMaxResults(pagesize);
			}
			JSONArray columns = GeneralUtil.getQueryColumns(sql, entityManager, null);
			List<Object[]> ls = q.getResultList();
			items = GeneralUtil.resultToJSONArray(columns, ls);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}

	public JSONArray getDataForView(JSONObject obj) {
		JSONArray rejson = new JSONArray();
		try {
			String moduleName = obj.get("module_name").toString();
			String sql = "";
			if (moduleName.equalsIgnoreCase("user")) {
				sql = "select a.first_name as \"First Name\",a.last_name as \"Last Name\",a.moblie_no as \"Mobile No.\", "
						+ " a.email as \"Email\",a.user_type as \"User Type\",fun_time(a.created_on) as \"Created Date\", "
						+ " fun_time(a.last_login) as \"Last Login\",a.image as \"image\" "
						+ " from users a where a.user_id=" + obj.get("user_id").toString();
			} else if (moduleName.equalsIgnoreCase("seller")) {
				sql = "select a.shop_name as \"Shop Name\",a.mobile_no as \"Mobile No.\",a.address as \"Address\",a.city as \"City\",a.state as \"State\", "
						+ " a.pin_code as \"Pin Code\",fun_time(a.created_on) as \"Created On\", a.image  as \"image\" from sellers a where a.seller_id="
						+ obj.get("seller_id").toString();
			} else if (moduleName.equalsIgnoreCase("stock_item")) {
				sql = "select b.name as \"Category\",c.name as \"Sub category\",d.name as \"Brand\",a.code as \"Item Code\",a.name as \"Item Name\",\n"
						+ "a.description as \"Description\",a.pack_size as \"Pack Size\",a.uom as \"UOM\",a.price as \"Price\",fun_time(a.created_on) as \"Created On\",a.image as \"image\" \n"
						+ "from stock_items a,category b,sub_category c,brands d where a.category_id=b.category_id\n"
						+ "and a.sub_cat_id=c.sub_cat_id and a.brand_id=d.brand_id(+) and a.stock_item_id="
						+ obj.get("stock_item_id").toString();
			}

			JSONArray columns = GeneralUtil.getQueryColumnsNoLower(sql, entityManager, null);
			List<Object[]> ls = entityManager.createNativeQuery(sql).getResultList();
			rejson = (JSONArray) GeneralUtil.resultToJSONArrayOfArray(columns, ls).get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rejson;
	}

	public JSONObject getDataForTable(JSONObject json) {
		JSONObject result = new JSONObject();
		try {
			String moduleName = json.get("module_name").toString();
			String sql = "";
			if (moduleName.equalsIgnoreCase("seller_users")) {
				sql = "select a.first_name as \"First Name\",a.last_name as \"Last Name\",a.moblie_no  as \"Mobile No\",a.email  as \"Email\",fun_time(a.last_login)  as \"Last Login\",a.user_type as \"User Type\" from users a,seller_user_map b "
						+ " where a.user_id=b.user_id and b.seller_id=" + GeneralUtil.objTolong(json.get("seller_id"))
						+ " order by a.first_name";
			} else if (moduleName.equalsIgnoreCase("seller_stock")) {
				sql = "select b.code as \"Item Code\",b.name as \"Item Name\",b.pack_size as \"Pack size\",b.uom as \"UOM\",a.quantity as \"Quantity\",a.price as \"Price\" from seller_stock_map a,stock_items b "
						+ " where a.stock_item_id=b.stock_item_id and a.seller_id= "
						+ GeneralUtil.objTolong(json.get("seller_id")) + " order by b.name ";
			} else if (moduleName.equalsIgnoreCase("address")) {
				sql = "select a.address as \"Address\",a.city as \"City\",a.state as \"State\",a.pin_code as \"Pin Code\"  from delivery_address a where a.user_id="
						+ GeneralUtil.objTolong(json.get("user_id")) + " order by a.address ";
			}
			JSONArray columns = GeneralUtil.getQueryColumnsNoLower(sql, entityManager, null);
			int pageFirst = 0;
			int pagesize = 0;
			Query q = entityManager.createNativeQuery(sql);
			if (json.containsKey("first")) {
				pageFirst = Integer.parseInt(json.get("first").toString());
			}
			if (json.containsKey("size")) {
				pagesize = Integer.parseInt(json.get("size").toString());
			}
			if (pagesize > 0) {
				q.setFirstResult(pageFirst).setMaxResults(pagesize);
			}
			List<Object[]> ls = q.getResultList();
			JSONArray data = GeneralUtil.resultToJSONArray(columns, ls);
			result.put("headers", columns);
			result.put("data", data);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public GeneralSetting getGeneralSetting(long sellerId) {
		GeneralSetting gen = null;
		try {
			String sql = "From GeneralSetting a where a.sellerId=" + sellerId;
			List<GeneralSetting> ls = entityManager.createQuery(sql).getResultList();
			logger.info(sql + "ls.size() " + ls.size());
			if (!ls.isEmpty()) {
				logger.info("ls.size() " + ls.size());
				gen = ls.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gen;
	}

	public DeliveryCharge getDeliveryCharge(long sellerId) {
		DeliveryCharge gen = null;
		try {
			String sql = "From DeliveryCharge a where a.sellerId=" + sellerId;
			List<DeliveryCharge> ls = entityManager.createQuery(sql).getResultList();
			if (!ls.isEmpty()) {
				gen = ls.get(0);
			}
		} catch (Exception e) {

		}
		return gen;
	}

	public String saveGeneralSettings(JSONObject json) {
		String result = "";
		try {
			GeneralSetting gen = getGeneralSetting(GeneralUtil.objTolong(json.get("sellerId")));

			if (gen == null) {
				gen = new GeneralSetting();
				ConvertJSONtoObject.jsonToObject(gen, json);
				gen.setGeneralSettingId(GeneralUtil.getPrimaryKey("SEQ_GEN_SETTINGS", entityManager));
				gen.setSellerId(GeneralUtil.objTolong(json.get("sellerId")));
				JSONObject m = (JSONObject) json.get("paymentModes");
				gen.setPaymentModes(ConvertJSONtoObject.multipleToJSONArray((JSONArray) m.get("value")));
				entityManager.persist(gen);
			} else {
				ConvertJSONtoObject.jsonToObject(gen, json);
				JSONObject m = (JSONObject) json.get("paymentModes");
				gen.setPaymentModes(ConvertJSONtoObject.multipleToJSONArray((JSONArray) m.get("value")));

				entityManager.merge(gen);
			}
			result = "success";
		} catch (Exception e) {
			e.printStackTrace();
			result = "failed";
		}
		return result;
	}

	public String saveDeliveryCharge(JSONObject json) {
		String result = "";
		try {
			DeliveryCharge gen = getDeliveryCharge(GeneralUtil.objTolong(json.get("sellerId")));

			if (gen == null) {
				gen = new DeliveryCharge();
				ConvertJSONtoObject.jsonToObject(gen, json);
				gen.setDeliveryChargeId(GeneralUtil.getPrimaryKey("SEQ_DELIVERY_CHARGES", entityManager));
				gen.setSellerId(GeneralUtil.objTolong(json.get("sellerId")));

				entityManager.persist(gen);
			} else {
				ConvertJSONtoObject.jsonToObject(gen, json);
				entityManager.merge(gen);
			}
			result = "success";
		} catch (Exception e) {
			result = "failed";
		}
		return result;
	}

	public JSONObject deliveryAddressChange(JSONObject json) {
		JSONObject obj = new JSONObject();
		try {
			long userId = GeneralUtil.objTolong(json.get("userId"));
			long delAdd = GeneralUtil.objTolong(json.get("address_id"));

			String sql1 = "update delivery_address a set a.is_default=decode(a.delivery_add_id," + delAdd
					+ ",1,0) where a.user_id=" + userId;
			logger.info("Sql : " + sql1);
			entityManager.createNativeQuery(sql1).executeUpdate();
			String sql = "select count(*) from cart_items a where a.address_id=" + delAdd + " and a.status='active'";
			List<Object> ls = entityManager.createNativeQuery(sql).getResultList();
			if (!ls.isEmpty())
				obj.put("cart_count", GeneralUtil.objToInt(ls.get(0)));
			else
				obj.put("cart_count", 0);

		} catch (Exception e) {
			// TODO: handle exception
		}
		return obj;
	}

	public JSONArray getSubscriptionPlans(JSONObject json) {
		JSONArray result = new JSONArray();
		try {
			String sql = "select a.subscription_plans_id,a.plan_types,a.name,a.duration_days,a.amount,a.image from subscription_plans a where 1=1 ";
			if (json.containsKey("subscription_plans_id")
					&& GeneralUtil.objTolong(json.get("subscription_plans_id")) > 0)
				sql += " and a.subscription_plans_id=" + GeneralUtil.objTolong(json.get("subscription_plans_id"));
			sql += " order by a.duration_days ";
			JSONArray columns = GeneralUtil.getQueryColumns(sql, entityManager, null);
			List<Object[]> ls = entityManager.createNativeQuery(sql).getResultList();
			result = GeneralUtil.resultToJSONArray(columns, ls);

		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public String saveSubscriptionPlans(JSONObject json) {
		String result = "";
		try {
			SubscriptionPlans gen = getSubscriptionPlans(0, GeneralUtil.objToString(json.get("name")), 0);

			if (gen == null) {
				gen = new SubscriptionPlans();
				ConvertJSONtoObject.jsonToObject(gen, json);
				gen.setSubscriptionPlanId(GeneralUtil.getPrimaryKey("SEQ_SUBSCRIPTION_PLANS", entityManager));
				if (((JSONObject) json.get("image")).get("value") != null
						&& ((JSONObject) json.get("image")).get("value").toString().length() > 0) {
					String image = GeneralUtil.writeImageFolder(
							((JSONObject) json.get("image")).get("value").toString(), "mycart/subscription/",
							gen.getSubscriptionPlanId() + "");
					gen.setImage(image);
				}
				entityManager.persist(gen);
				result = "success";
			} else {
				result = "Subscription plan name already exist";
			}

		} catch (Exception e) {
			result = "failed";
		}
		return result;
	}

	public String updateSubscriptionPlans(JSONObject json) {
		String result = "";
		try {
			JSONObject j = (JSONObject) json.get("subscription_plans_id");
			long id = GeneralUtil.objTolong(j.get("value"));
			logger.info("subscription_plans_id  : " + json.get("subscription_plans_id").toString());
			SubscriptionPlans gen1 = new SubscriptionPlans();
			ConvertJSONtoObject.jsonToObject(gen1, json);
			SubscriptionPlans gen = getSubscriptionPlans(id, gen1.getName(), 1);

			if (gen == null) {
				gen = getSubscriptionPlans(id, null, 0);
				ConvertJSONtoObject.jsonToObject(gen, json);
				if (((JSONObject) json.get("image")).get("value") != null
						&& ((JSONObject) json.get("image")).get("value").toString().length() > 0) {
					String image = GeneralUtil.writeImageFolder(
							((JSONObject) json.get("image")).get("value").toString(), "mycart/subscription/",
							gen.getSubscriptionPlanId() + "");
					gen.setImage(image);
					logger.info("Image path : " + image);
				}
				entityManager.merge(gen);
				result = "success";
			} else {
				result = "Subscription plan name already exist";
			}

		} catch (Exception e) {
			result = "failed";
		}
		return result;
	}

	public SubscriptionPlans getSubscriptionPlans(long id, String name, int type) {
		SubscriptionPlans sub = null;
		try {
			String sql = "From SubscriptionPlans a where 1=1 ";
			if (id > 0)
				sql += " and a.subscriptionPlanId " + (type == 0 ? "=" : "!=") + " " + id;
			if (name != null && name.trim().length() > 0)
				sql += " and a.name=:plan_name";

			logger.info("Sql " + sql);
			List<SubscriptionPlans> ls = entityManager.createQuery(sql).getResultList();
			if (!ls.isEmpty())
				sub = ls.get(0);
		} catch (Exception e) {

		}
		return sub;
	}

	public JSONObject addsubscription(JSONObject json) {
		JSONObject result = new JSONObject();
		try {
			long subId = GeneralUtil.objTolong(json.get("subscription_plans_id"));
			long userId = GeneralUtil.objTolong(json.get("user_id"));
			long sellerId = GeneralUtil.objTolong(json.get("seller_id"));
			Seller s = getSeller(sellerId);
			SubscriptionPlans sp = getSubscriptionPlans(subId, null, 0);
			long days = GeneralUtil.daysBetweenFromCurrentDate(s.getExpiredDate());
			Date startDate = null;
			Date endDate = null;
			String status = "";
			if (days < 0) {
				startDate = new Date();
				endDate = GeneralUtil.addingDaysToDate(startDate, sp.getDurationDays() - 1);
				status = "active";
				String sql = "update sellers a set a.subscription_plans_id=" + subId
						+ ",a.sub_date=sysdate,a.expired_date=to_date('" + GeneralUtil.dateToString(endDate)
						+ "','dd-mm-yyyy')  " + " where a.seller_id=" + sellerId;
				entityManager.createNativeQuery(sql).executeUpdate();
				sql = "update subscription_details a set a.status='closed' where a.seller_id=" + sellerId
						+ " and a.status='active'";
				entityManager.createNativeQuery(sql).executeUpdate();
				result.put("exp_date", GeneralUtil.dateToString(endDate));
				result.put("cur_days", sp.getDurationDays());

			} else {
				startDate = GeneralUtil.addingDaysToDate(s.getExpiredDate(), 1);
				endDate = GeneralUtil.addingDaysToDate(startDate, sp.getDurationDays() - 1);
				status = "pending";
				result.put("exp_date", GeneralUtil.dateToString(s.getExpiredDate()));
			}
			SubcriptionDetails sub = new SubcriptionDetails();
			sub.setSubscriptionDetailsId(GeneralUtil.getPrimaryKey("SEQ_SUBSCRIPTION_DETAILS", entityManager));
			sub.setSubscriptionPlanId(subId);
			sub.setSellerId(sellerId);
			sub.setAmount(sp.getAmount());
			sub.setStartDate(startDate);
			sub.setEndDate(endDate);
			sub.setPaidDate(new Date());
			sub.setUserId(userId);
			sub.setStatus(status);
			sub.setDurationDays(sp.getDurationDays());
			entityManager.persist(sub);
			result.put("reason", "success");

		} catch (Exception e) {
			result.put("reason", "failed");
		}
		return result;
	}

	public JSONArray getSubcriptionDetails(JSONObject json) {
		JSONArray result = new JSONArray();
		try {
			String sql = "select b.subscription_plans_id,a.amount,b.name,b.duration_days,fun_date(a.paid_date) paid_date,fun_date(a.start_date) start_date,fun_date(a.end_date) end_date,a.status,b.image from subscription_details a,subscription_plans b  "
					+ " where a.subscription_plans_id=b.subscription_plans_id ";
			if (GeneralUtil.objTolong(json.get("sub_id")) > 0)
				sql += " and a.subscription_plans_id=" + GeneralUtil.objTolong(json.get("sub_id"));
			if (GeneralUtil.objTolong(json.get("seller_id")) > 0)
				sql += " and a.seller_id=" + GeneralUtil.objTolong(json.get("seller_id"));
			if (GeneralUtil.objToString(json.get("status")).length() > 0)
				sql += " and a.status='" + GeneralUtil.objToString(json.get("status")) + "'";
			sql += " order by a.start_date";
			logger.info("Sql : " + sql);
			Query q = entityManager.createNativeQuery(sql);
			JSONArray columns = GeneralUtil.getQueryColumns(sql, entityManager, null);
			List<Object[]> ls = q.getResultList();
			result = GeneralUtil.resultToJSONArray(columns, ls);

		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public SubcriptionDetails getSubcriptionDetails(long id, long sellerId, String status) {
		try {
			String sql = "From SubcriptionDetails a where 1=1 ";
			if (id > 0)
				sql += " and a.subscriptionDetailsId=" + id;
			if (sellerId > 0)
				sql += " and a.sellerId=" + sellerId;
			if (status != null)
				sql += " and a.status='" + status + "'";
			List<SubcriptionDetails> ls = entityManager.createQuery(sql).getResultList();
			if (!ls.isEmpty())
				return ls.get(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;

	}

}
