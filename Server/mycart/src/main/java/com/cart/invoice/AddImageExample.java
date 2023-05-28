package com.cart.invoice;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.cart.util.GeneralUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class AddImageExample {
	public static void main(String[] args) {
		Document document = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("D:/AddImageExample.pdf"));
			document.open();
			File f = new File("E:\\hindi");
			for (File c : f.listFiles()) {
				Image image1 = Image.getInstance(c.getAbsolutePath());
				image1.scaleAbsolute(58f, 58f);
				document.add(image1);

			}
			document.newPage();
			addTable(document);

			document.close();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addTable(Document document) {
		try {
			PdfPTable table = new PdfPTable(6); // 3 columns.
			table.setWidthPercentage(100); // Width 100%
			table.setSpacingBefore(10f); // Space before table
			float[] columnWidths = { 1f, 2f, 4f, 1f, 1.2f, 1f, 1.2f };
			// table.setWidths(columnWidths);
			File f = new File("E:\\hindi");
			int index = 0;
			for (File c : f.listFiles()) {
				Image image1 = Image.getInstance(c.getAbsolutePath());
				image1.scaleAbsolute(55f, 55f);
				table.addCell(image1);
				index++;
				if (index % 6 == 0) {
					for (int i = 0; i < 6; i++) {
						Image image2 = Image.getInstance("E:\\Movies\\Image 2.jpg");
						table.addCell(image2);

					}
				}
				if (index == 24) {
					for (int i = 0; i < 6; i++) {
						table.addCell("     ");
					}
				}

			}
			document.add(table);

		} catch (Exception ex) {
			ex.printStackTrace();

		}

	}

}
