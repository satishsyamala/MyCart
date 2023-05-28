package com.cart.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cart.controller.UserController;
import com.cart.dao.intf.DeliveryDaoIntf;
import com.cart.pojo.OrderTrack;
import com.cart.pojo.UserLatestInfo;
import com.cart.util.GeneralUtil;

@Repository
public class DeliveryDaoImpl implements DeliveryDaoIntf {

	private static final Logger logger = LogManager.getLogger(DeliveryDaoImpl.class);
	@Autowired
	private EntityManager entityManager;

	@Autowired
	private TransactionsDaoImpl transactionsDaoImpl;

	@Override
	public JSONObject locationData(JSONObject json) {
		JSONObject result = new JSONObject();
		result.put("notification", "no");
		try {
			long uli = GeneralUtil.objTolong(json.get("user_id"));

			String sql = "update user_latest_info a set a.latitude='" + GeneralUtil.objToString(json.get("latitude"))
					+ "',a.longitude='" + GeneralUtil.objToString(json.get("longitude"))
					+ "',a.last_location_date=sysdate\r\n" + "where a.user_id=" + uli;
			int con = entityManager.createNativeQuery(sql).executeUpdate();
			if (con > 0) {
				sql = "select a.accepted_orders from user_latest_info a where a.user_id=" + uli
						+ " and a.notification_status=1";
				List<Object[]> ls = entityManager.createNativeQuery(sql).getResultList();
				if (!ls.isEmpty()) {
					result.put("notification", "yes");
					result.put("orderds", getAssignedOrder(json, "assigned"));

				}
			} else {
				UserLatestInfo u = new UserLatestInfo();
				u.setUserlastInfoId(GeneralUtil.getPrimaryKey("SEQ_USER_LATEST_INFO", entityManager));
				uli = u.getUserlastInfoId();
				u.setUserId(GeneralUtil.objTolong(json.get("user_id")));
				u.setLatitude(GeneralUtil.objToString(json.get("latitude")));
				u.setLongitude(GeneralUtil.objToString(json.get("longitude")));
				u.setLastLocationDate(new Date());
				u.setIsActive(1);
				entityManager.persist(u);
			}

			result.put("reason", "success");
			result.put("latest_info_id", uli);
		} catch (Exception e) {
			result.put("reason", "failed");
		}
		return result;
	}

	public JSONArray getAssignedOrder(JSONObject json, String status) {
		JSONArray result = new JSONArray();
		try {
			long userId = GeneralUtil.objTolong(json.get("user_id"));
			String sql = "select b.order_no,b.order_id,a.order_delivery_user_map_id, c.seller_id,d.address||', '||d.city||' '||d.state||'-'||d.pin_code delivery_address, "
					+ " d.latitude del_lat,d.longitude del_long, c.address||' '||c.city||' '||c.state||'-'||c.pin_code sel_addres, "
					+ " c.lotitude sel_lat,c.longitude sel_lon,c.shop_name,c.mobile_no,c.image,a.user_id,a.assign_status,b.order_id||'' qr_code,"
					+ "   a.pickup_otp,b.status order_status,u.FIRST_NAME||' '||u.LAST_NAME full_name,u.MOBLIE_NO,b.PAYMENT_TYPE,b.PRICE "
					+ " from order_delivery_user_map a,orders b,sellers c,delivery_address d,users u where b.user_id=u.user_id and a.user_id=" + userId;
			if (status != null && status.length() > 0) {
				if (status.equalsIgnoreCase("both"))
					sql += " and a.assign_status in ('Accept','Pickup')";
				else
					sql += " and a.assign_status='" + status + "'";
			}
			sql += " and a.order_id=b.order_id  and c.seller_id=b.seller_id and b.delivery_add_id=d.delivery_add_id ";

			JSONArray columns = GeneralUtil.getQueryColumns(sql, entityManager, null);
			List<Object[]> ls = entityManager.createNativeQuery(sql).getResultList();
			result = GeneralUtil.resultToJSONArray(columns, ls);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public JSONObject acceptOrder(JSONObject json) {
		JSONObject result = new JSONObject();
		try {
			String status = json.get("action").toString();
			long otp = 0;
			String sql = "";
			boolean check = true;
			if (status.equalsIgnoreCase("Accept") || status.equalsIgnoreCase("Reject")) {
				otp = GeneralUtil.otpGenerate(1001, 9999);
				sql = "update order_delivery_user_map a set a.assign_status='" + status
						+ "',a.accepted_date=sysdate,a.PICKUP_OTP=" + otp
						+ " where a.assign_status='assigned' and a.order_delivery_user_map_id="
						+ GeneralUtil.objTolong(json.get("order_delivery_user_map_id"));
			} else if (status.equalsIgnoreCase("Pickup")) {
				check = checkOrderStatus(GeneralUtil.objTolong(json.get("order_id")), "Out For Delivery");
				if (check)
					sql = "update order_delivery_user_map a set a.assign_status='" + status
							+ "',a.pick_up_date=sysdate where a.assign_status='Accept' and a.order_delivery_user_map_id="
							+ GeneralUtil.objTolong(json.get("order_delivery_user_map_id"));
				else
					result.put("reason", "Order is Waiting For Out for Delivery");
			}
			if (check) {
				int con = entityManager.createNativeQuery(sql).executeUpdate();
				if (con > 0 && (status.equalsIgnoreCase("Accept") || status.equalsIgnoreCase("Reject"))) {
					String sql1 = "update user_latest_info a set a.notification_status=0 where a.user_id="
							+ GeneralUtil.objTolong(json.get("user_id"));
					entityManager.createNativeQuery(sql1).executeUpdate();
					if (status.equalsIgnoreCase("Reject")) {
						long userId = GeneralUtil.objTolong(json.get("user_id"));
						long orderId = GeneralUtil.objTolong(json.get("order_id"));
						long assUserId = transactionsDaoImpl.assignedTodeliveryUser(orderId, userId,null);
						if (assUserId == 0) {
							sql = "update orders a set a.status='Accepted',a.track_status='Accepted' where a.order_id="
									+ orderId + " and a.status='Assigned' ";
							con = entityManager.createNativeQuery(sql).executeUpdate();

						}
					}
					
				} else {
					result.put("reason", "Order accepted by other user");
				}
				result.put("reason", "success");
			}

		} catch (Exception e) {
			// TODO: handle exception
			result.put("reason", "failed");
		}
		return result;
	}

	public boolean checkOrderStatus(long orderId, String status) {
		boolean result = false;
		try {
			String sql = "select a.order_id from orders a where a.order_id=" + orderId + " and a.status='" + status
					+ "'";
			logger.info("SQl : " + sql);
			List<Object> ls = entityManager.createNativeQuery(sql).getResultList();
			if (!ls.isEmpty()) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public JSONObject userActiveOrInActive(JSONObject json)
	{
		JSONObject result = new JSONObject();
		try {
			String sql = "update user_latest_info a set a.is_active="+GeneralUtil.objToInt(json.get("active"))+" where a.user_id="+GeneralUtil.objToInt(json.get("user_id"));
			logger.info("SQl : " + sql);
			int count= entityManager.createNativeQuery(sql).executeUpdate();
			if(count>0)
				result.put("reason", "success");
			else
				result.put("reason", "failed");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
