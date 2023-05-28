/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cart.invoice;

import com.cart.file.ExcelReadAndWrite;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class CreateBarcodePdf {

	public static void main(String... args) throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("D:/barcode_3.pdf"));

		document.open();
		PdfContentByte cb = writer.getDirectContent();

		java.util.List<java.util.List<String>> data = ExcelReadAndWrite.getExcelXLSX(new File("d:/Stocks.xls"));
		int y = 700;
		int x = 1;
		Set<String> s = new HashSet<>();
		for (java.util.List<String> ls : data) {
			if (x < 25) {
				Barcode128 barcode128 = new Barcode128();
				String s1 = ls.get(1).substring(0, 2);
				if (!s.contains(s1)) {
					s.add(s1);
					barcode128.setCode(ls.get(3));
					//System.out.println(ls.get(3));
					barcode128.setCodeType(Barcode.CODE128);
					Image code128Image = barcode128.createImageWithBarcode(cb, null, null);
					createContent(cb, (x % 2 == 0 ? 350 : 50), y+100, ls.get(1), Element.ALIGN_LEFT, 8);
					code128Image.setAbsolutePosition((x % 2 == 0 ? 350 : 50), y);
					code128Image.scaleToFit(180, 100);
					document.add(code128Image);
					if (x % 2 == 0) {
						y = y - 120;
						if (y < 100) {
							document.newPage();
							y = 700;
						}
					}
					x++;
					System.out.println(ls.get(0)+","+ls.get(1)+","+ls.get(2)+","+ls.get(3));
				}
			}
		}

		Barcode128 barcode128 = new Barcode128();
		barcode128.setCode("1234567890");
		barcode128.setCodeType(Barcode.CODE128);
		Image code128Image = barcode128.createImageWithBarcode(cb, null, null);
		code128Image.setAbsolutePosition(x, y);
		code128Image.scaleToFit(190, 190);

		// document.add(code128Image);
		;
		// document.newPage();

		Barcode39 barcode39 = new Barcode39();
		barcode39.setCode("202108011229372");
		Image code39Image = barcode39.createImageWithBarcode(cb, null, null);
		// document.add(code39Image);
		// document.newPage();

		BarcodeEAN barcodeEAN = new BarcodeEAN();
		barcodeEAN.setCode("202108011229372");
		barcodeEAN.setCodeType(Barcode.EAN13);
		Image codeEANImage = barcodeEAN.createImageWithBarcode(cb, null, null);
		// document.add(codeEANImage);
		// document.newPage();

		BarcodeQRCode barcodeQRCode = new BarcodeQRCode("202108011229372", 1000, 1000, null);
		Image codeQrImage = barcodeQRCode.getImage();
		codeQrImage.scaleAbsolute(100, 100);
		// document.add(codeQrImage);

		document.close();
	}

	private static void createContent(PdfContentByte cb, float x, float y, String text, int align, int size) {
		cb.beginText();
		cb.setFontAndSize(getBaseFont(), size);
		cb.showTextAligned(align, text.trim(), x, y, 0);
		cb.endText();
	}

	private static BaseFont getBaseFont() {
		try {
			return BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		} catch (Exception ex) {
			return null;
		}
	}

}
