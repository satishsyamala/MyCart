/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aqua.invoice;

import com.aqua.util.GeneralUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.Barcode39;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author USER
 */
public class OrderPDF {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(OrderPDF.class);

    public static File orderPdf(JSONObject json) {
        logger.info("JSON  : " + json.toString());
        Document document = new Document();

        String basePath = GeneralUtil.getPropertyValue("basepath") + "mycart/orders/" + json.get("moblie_no").toString();
        File dir = new File(basePath);
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
        File pdf = new File(basePath + "/" + json.get("order_no").toString() + ".pdf");
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdf));

            document.open();
            PdfContentByte cb = writer.getDirectContent();
            Rectangle pageSize = new Rectangle(0, 780, 600, 850);
            pageSize.setBackgroundColor(BaseColor.ORANGE);
            document.add(pageSize);
            Image img = getImage("logo2.png");
            img.scaleToFit(40, 40);
            img.setAbsolutePosition(20, 790);
            document.add(img);
            document.add(new Paragraph("   "));
            createContent(cb, 22, 785, "MyCart", Element.ALIGN_LEFT, 10);
            if (json.get("status").toString().equalsIgnoreCase("delivered")) {
                createContentheader(cb, 400, 790, "Invoice", Element.ALIGN_LEFT, 20);
                createContent(cb, 40, 750, "Invoice No ", Element.ALIGN_LEFT, 10);
            } else {
                createContentheader(cb, 400, 790, "Order Details", Element.ALIGN_LEFT, 20);
                createContent(cb, 40, 750, "Order No ", Element.ALIGN_LEFT, 10);
            }
            addbarcode(document, cb, json.get("order_no").toString(), 320, 710);
            //createContent(cb, 40, 750, "Order No ", Element.ALIGN_LEFT, 10);
            createContent(cb, 120, 750, ": " + json.get("order_no").toString(), Element.ALIGN_LEFT, 10);
            createContent(cb, 40, 730, "Item Name ", Element.ALIGN_LEFT, 10);
            createContent(cb, 120, 730, ": " + json.get("name").toString(), Element.ALIGN_LEFT, 10);
            createContent(cb, 40, 710, "Order Date ", Element.ALIGN_LEFT, 10);
            createContent(cb, 120, 710, ": " + json.get("order_date").toString(), Element.ALIGN_LEFT, 10);
            if (json.get("status").toString().equalsIgnoreCase("delivered")) {
                createContent(cb, 40, 690, "Delivered Date ", Element.ALIGN_LEFT, 10);
                createContent(cb, 120, 690, ": " + json.get("act_delivery_date").toString(), Element.ALIGN_LEFT, 10);
            } else {
                createContent(cb, 40, 690, "Delivery Date ", Element.ALIGN_LEFT, 10);
                createContent(cb, 120, 690, ": " + json.get("delivery_date").toString(), Element.ALIGN_LEFT, 10);
            }
            createContent(cb, 40, 670, "Status ", Element.ALIGN_LEFT, 10);
            createContent(cb, 120, 670, ": " + json.get("status").toString(), Element.ALIGN_LEFT, 10);

            createContent(cb, 320, 690, "Payment Type", Element.ALIGN_LEFT, 10);
            createContent(cb, 400, 690, ": " + json.get("payment_type").toString(), Element.ALIGN_LEFT, 10);
            createContent(cb, 320, 670, "Delivery Type ", Element.ALIGN_LEFT, 10);
            createContent(cb, 400, 670, ": " + json.get("delivery_type").toString(), Element.ALIGN_LEFT, 10);

            PdfPTable table = new PdfPTable(2); // 3 columns.
            table.setWidthPercentage(100); //Width 100%
            table.setSpacingBefore(150f); //Space before table
            float[] columnWidths = {1f, 1f};
            table.setWidths(columnWidths);
            table.addCell(getPCell("Delivery", Element.ALIGN_CENTER));
            table.addCell(getPCell("Seller", Element.ALIGN_CENTER));
            String text = json.get("full_name").toString() + " \n" + "Mobile No. : " + json.get("moblie_no").toString() + " \n" + json.get("address").toString();
            table.addCell(getPCell(text, Element.ALIGN_LEFT));
            JSONObject sel = (JSONObject) json.get("seller");
            text = sel.get("shop_name").toString() + " \n" + "Mobile No. : " + sel.get("mobile_no").toString() + " \n" + sel.get("address").toString();
            table.addCell(getPCell(text, Element.ALIGN_LEFT));
            document.add(table);

            addTable(document, (JSONArray) json.get("items"), json);
            document.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pdf;
    }

    private static void createContent(PdfContentByte cb, float x, float y, String text, int align, int size) {
        cb.beginText();
        cb.setFontAndSize(getBaseFont(), size);
        cb.showTextAligned(align, text.trim(), x, y, 0);
        cb.endText();
    }

    private static void createContentheader(PdfContentByte cb, float x, float y, String text, int align, int size) {
        cb.beginText();
        cb.setFontAndSize(getHeaderBaseFont(), size);
        cb.showTextAligned(align, text.trim(), x, y, 0);
        cb.endText();
    }

    private static BaseFont getHeaderBaseFont() {
        try {
            return BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        } catch (Exception ex) {
            return null;
        }
    }

    private static BaseFont getBaseFont() {
        try {
            return BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        } catch (Exception ex) {
            return null;
        }
    }

    public static void addbarcode(Document document, PdfContentByte cb, String text, int x, int y) {
        try {

         
        Barcode128 barcode128 = new Barcode128();
        barcode128.setCode(text);
        barcode128.setCodeType(Barcode.CODE128);
        Image code128Image = barcode128.createImageWithBarcode(cb, null, null);
        code128Image.setAbsolutePosition(x, y);
        code128Image.scaleToFit(190, 190);
        document.add(code128Image);
            
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    public static Image getImage(String name) {
        try {
            File resource = new ClassPathResource("images/" + name).getFile();
            logger.info("Path : " + resource.getAbsolutePath());
            return Image.getInstance(resource.getAbsolutePath());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public static void addTable(Document document, JSONArray items, JSONObject json) {
        try {
            PdfPTable table = new PdfPTable(7); // 3 columns.
            table.setWidthPercentage(100); //Width 100%
            table.setSpacingBefore(10f); //Space before table
            float[] columnWidths = {1f, 2f, 4f, 1f, 1.2f, 1f, 1.2f};
            table.setWidths(columnWidths);
            table.addCell(getPCell("Sr.No.", Element.ALIGN_LEFT));
            table.addCell(getPCell("Category", Element.ALIGN_LEFT));
            table.addCell(getPCell("Name", Element.ALIGN_LEFT));
            table.addCell(getPCell("UOM", Element.ALIGN_LEFT));
            table.addCell(getPCell("Qty.", Element.ALIGN_RIGHT));
            table.addCell(getPCell("Price", Element.ALIGN_RIGHT));
            table.addCell(getPCell("Amount", Element.ALIGN_RIGHT));

            for (int i = 0; i < items.size(); i++) {
                JSONObject item = (JSONObject) items.get(i);
                table.addCell(getPCell((i + 1) + "", Element.ALIGN_RIGHT));
                table.addCell(getPCell(item.get("cat_name").toString(), Element.ALIGN_LEFT));
                table.addCell(getPCell(item.get("name").toString(), Element.ALIGN_LEFT));
                table.addCell(getPCell(item.get("pack_uom").toString(), Element.ALIGN_LEFT));
                table.addCell(getPCell(item.get("quantity").toString(), Element.ALIGN_RIGHT));
                table.addCell(getPCell((GeneralUtil.objTolong(item.get("price")) / GeneralUtil.objTolong(item.get("quantity"))) + "", Element.ALIGN_RIGHT));
                table.addCell(getPCell(item.get("final_price").toString(), Element.ALIGN_RIGHT));
            }
            document.add(table);
            float[] columnWidths1 = {2.2f, 1.2f};
            PdfPTable table1 = new PdfPTable(2); // 3 columns.
            table1.setWidthPercentage(30); //Width 100%
            table1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table1.setWidths(columnWidths1);
            table1.addCell(getPCell("Sub total", Element.ALIGN_LEFT));
            table1.addCell(getPCell(json.get("price").toString(), Element.ALIGN_RIGHT));
            table1.addCell(getPCell("Discount", Element.ALIGN_LEFT));
            table1.addCell(getPCell(json.get("discount").toString(), Element.ALIGN_RIGHT));
            table1.addCell(getPCell("Delv. Charge", Element.ALIGN_LEFT));
            table1.addCell(getPCell(json.get("delivery_charge").toString(), Element.ALIGN_RIGHT));
            table1.addCell(getPCell("Final Price", Element.ALIGN_LEFT));
            table1.addCell(getPCell(json.get("final_price").toString(), Element.ALIGN_RIGHT));
            document.add(table1);

        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }

    public static PdfPCell getPCell(String text, int align, int barder) {
        PdfPCell cell3 = new PdfPCell(new Paragraph(text));
        cell3.setPaddingLeft(10);
        cell3.setHorizontalAlignment(align);
        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell3.setBorder(barder);
        return cell3;
    }

    public static PdfPCell getPCell(String text, int align) {
        PdfPCell cell3 = new PdfPCell(new Paragraph(text));
        cell3.setPaddingLeft(10);
        cell3.setHorizontalAlignment(align);
        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

        return cell3;
    }

    public static void main(String[] args) {
        try {
            String j = "{\"order_no\":\"202108011229372\",\"seller\":{\"licence_no\":\"\",\"address\":\"Banjarahiils Hyd, Hyderabad, Telangana-500036\",\"mobile_no\":\"9959810024\",\"lotitude\":\"18.121937030919728\",\"shop_name\":\"Big Mart\",\"seller_id\":1,\"longitude\":\"79.43463988078044\"},\"image\":\"mycart\\/category\\/12.jpeg\",\"address\":\"Banjarahiils11, Hyderabad, Telangana, 500036\",\"act_delivery_date\":\"\",\"discount\":0,\"otp\":0,\"order_date\":\"01-08-2021 12:29\",\"delivery_date\":\"05-08-2021\",\"moblie_no\":\"9618027500\",\"payment_type\":\"COD\",\"full_name\":\"Bindhu Syamala\",\"final_price\":2256,\"price\":2256,\"delivery_type\":\"Delivery\",\"name\":\"Veg & Fruits\",\"track_status\":\"Order Placed\",\"track\":[{\"date\":\"01-08-2021 12:29\",\"color\":\"#0ec9e6\",\"icon\":\"order-placed\",\"description\":\"Order Placed\",\"status\":\"Order Placed\"},{\"date\":\"\",\"color\":\"\",\"icon\":\"accepted\",\"status\":\"Accepted\"},{\"date\":\"\",\"color\":\"\",\"icon\":\"packed\",\"status\":\"Packed\"},{\"date\":\"\",\"color\":\"\",\"icon\":\"outfordelivery\",\"status\":\"Out For Delivery\"},{\"date\":\"\",\"color\":\"\",\"icon\":\"delivered\",\"status\":\"Delivered\"}],\"order_id\":110,\"items\":[{\"order_item_id\":173,\"image\":\"mycart\\/stock\\/20\\/1601.jpeg\",\"uom\":\"Kg\",\"quantity\":6,\"final_price\":720,\"cat_name\":\"Veg & Fruits\",\"pack_size\":1,\"price\":720,\"name\":\"Apple\",\"stock_item_id\":1601,\"diccount\":0,\"pack_uom\":\"1Kg\"},{\"order_item_id\":174,\"image\":\"mycart\\/stock\\/20\\/1602.jpeg\",\"uom\":\"Kg\",\"quantity\":1,\"final_price\":50,\"cat_name\":\"Veg & Fruits\",\"pack_size\":1,\"price\":50,\"name\":\"Bananas\",\"stock_item_id\":1602,\"diccount\":0,\"pack_uom\":\"1Kg\"},{\"order_item_id\":182,\"image\":\"mycart\\/stock\\/20\\/1603.jpeg\",\"uom\":\"Kg\",\"quantity\":1,\"final_price\":100,\"cat_name\":\"Veg & Fruits\",\"pack_size\":1,\"price\":100,\"name\":\"Grapes\",\"stock_item_id\":1603,\"diccount\":0,\"pack_uom\":\"1Kg\"},{\"order_item_id\":183,\"image\":\"mycart\\/stock\\/20\\/1604.jpeg\",\"uom\":\"Kg\",\"quantity\":3,\"final_price\":270,\"cat_name\":\"Veg & Fruits\",\"pack_size\":1,\"price\":270,\"name\":\"Guava\",\"stock_item_id\":1604,\"diccount\":0,\"pack_uom\":\"1Kg\"},{\"order_item_id\":185,\"image\":\"mycart\\/stock\\/20\\/1605.jpeg\",\"uom\":\"Kg\",\"quantity\":1,\"final_price\":60,\"cat_name\":\"Veg & Fruits\",\"pack_size\":1,\"price\":60,\"name\":\"Mango\",\"stock_item_id\":1605,\"diccount\":0,\"pack_uom\":\"1Kg\"},{\"order_item_id\":186,\"image\":\"mycart\\/stock\\/20\\/1606.jpeg\",\"uom\":\"Pc\",\"quantity\":3,\"final_price\":120,\"cat_name\":\"Veg & Fruits\",\"pack_size\":1,\"price\":120,\"name\":\"Pineapple\",\"stock_item_id\":1606,\"diccount\":0,\"pack_uom\":\"1Pc\"},{\"order_item_id\":188,\"image\":\"mycart\\/stock\\/20\\/1607.jpeg\",\"uom\":\"Kg\",\"quantity\":3,\"final_price\":240,\"cat_name\":\"Veg & Fruits\",\"pack_size\":1,\"price\":240,\"name\":\"Watermelon\",\"stock_item_id\":1607,\"diccount\":0,\"pack_uom\":\"1Kg\"},{\"order_item_id\":179,\"image\":\"mycart\\/stock\\/21\\/1608.jpeg\",\"uom\":\"Pc\",\"quantity\":1,\"final_price\":40,\"cat_name\":\"Veg & Fruits\",\"pack_size\":1,\"price\":40,\"name\":\"Cabbage\",\"stock_item_id\":1608,\"diccount\":0,\"pack_uom\":\"1Pc\"},{\"order_item_id\":175,\"image\":\"mycart\\/stock\\/21\\/1609.jpeg\",\"uom\":\"Kg\",\"quantity\":1,\"final_price\":80,\"cat_name\":\"Veg & Fruits\",\"pack_size\":1,\"price\":80,\"name\":\"Beans\",\"stock_item_id\":1609,\"diccount\":0,\"pack_uom\":\"1Kg\"},{\"order_item_id\":176,\"image\":\"mycart\\/stock\\/21\\/1610.jpeg\",\"uom\":\"Kg\",\"quantity\":1,\"final_price\":30,\"cat_name\":\"Veg & Fruits\",\"pack_size\":1,\"price\":30,\"name\":\"Bottle gourd\",\"stock_item_id\":1610,\"diccount\":0,\"pack_uom\":\"1Kg\"},{\"order_item_id\":180,\"image\":\"mycart\\/stock\\/21\\/1611.jpeg\",\"uom\":\"Kg\",\"quantity\":4,\"final_price\":200,\"cat_name\":\"Veg & Fruits\",\"pack_size\":1,\"price\":200,\"name\":\"Brinjal\",\"stock_item_id\":1611,\"diccount\":0,\"pack_uom\":\"1Kg\"},{\"order_item_id\":178,\"image\":\"mycart\\/stock\\/21\\/1612.jpeg\",\"uom\":\"Kg\",\"quantity\":1,\"final_price\":80,\"cat_name\":\"Veg & Fruits\",\"pack_size\":1,\"price\":80,\"name\":\"Carrot\",\"stock_item_id\":1612,\"diccount\":0,\"pack_uom\":\"1Kg\"},{\"order_item_id\":177,\"image\":\"mycart\\/stock\\/21\\/1613.jpeg\",\"uom\":\"Pc\",\"quantity\":4,\"final_price\":180,\"cat_name\":\"Veg & Fruits\",\"pack_size\":1,\"price\":180,\"name\":\"Cauliflower\",\"stock_item_id\":1613,\"diccount\":0,\"pack_uom\":\"1Pc\"},{\"order_item_id\":181,\"image\":\"mycart\\/stock\\/21\\/1614.jpeg\",\"uom\":\"Kg\",\"quantity\":1,\"final_price\":20,\"cat_name\":\"Veg & Fruits\",\"pack_size\":1,\"price\":20,\"name\":\"Drumstick\",\"stock_item_id\":1614,\"diccount\":0,\"pack_uom\":\"1Kg\"},{\"order_item_id\":184,\"image\":\"mycart\\/stock\\/21\\/1615.jpeg\",\"uom\":\"Kg\",\"quantity\":1,\"final_price\":46,\"cat_name\":\"Veg & Fruits\",\"pack_size\":1,\"price\":46,\"name\":\"Ladies finger\",\"stock_item_id\":1615,\"diccount\":0,\"pack_uom\":\"1Kg\"},{\"order_item_id\":187,\"image\":\"mycart\\/stock\\/21\\/1616.jpeg\",\"uom\":\"Kg\",\"quantity\":1,\"final_price\":20,\"cat_name\":\"Veg & Fruits\",\"pack_size\":1,\"price\":20,\"name\":\"Tomato\",\"stock_item_id\":1616,\"diccount\":0,\"pack_uom\":\"1Kg\"}],\"transaction_no\":\"202108011229372\",\"seller_id\":1,\"status\":\"Delivered\"}";
            JSONParser p = new JSONParser();
            JSONObject json = (JSONObject) p.parse(j);
            orderPdf(json);
        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }

}
