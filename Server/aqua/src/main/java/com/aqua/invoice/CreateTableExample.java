package com.aqua.invoice;

import java.io.File;
import java.io.FileOutputStream;

import org.springframework.core.io.ClassPathResource;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class CreateTableExample {

	public static void main(String[] args) {
		Document document = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("D:/AddTableExample1.pdf"));
			document.open();
			PdfContentByte cb = writer.getDirectContent();
			lines4(cb);
			document.newPage();
			box(cb);
			document.newPage();
			boxlines4(cb);
			document.newPage();
			mathsBoxes(cb);
			images(document, cb);
			document.newPage();
			mathsBoxes(cb);
			minusimages(document, cb);
			document.close();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void lines4(PdfContentByte cb) {
		cb.setLineWidth(1f);
		int min = 0;
		for (int i = 0; i < 14; i++) {
			for (int j = 0; j < 4; j++) {
				if (j == 0 || j == 3) {
					cb.setRGBColorStrokeF(255f, 0f, 0f);
				} else {
					cb.setRGBColorStrokeF(0f, 0f, 255f);
				}
				cb.moveTo(10, 820 - min);
				cb.lineTo(580, 820 - min);
				cb.stroke();
				min += 15;
			}
			min += 10;
		}
		cb.stroke();
	}

	public static void boxlines4(PdfContentByte cb) {
		cb.setLineWidth(1f);
		int min = 0;
		for (int i = 0; i < 12; i++) {
			int lnmin = 0;
			int inmin = min;
			for (int k = 0; k < 5; k++) {
				inmin = min;
				cb.setRGBColorStrokeF(255f, 0f, 0f);
				cb.moveTo(10 + lnmin, 820 - min);
				cb.lineTo(10 + lnmin, (820 - 45 - min));
				cb.stroke();
				for (int j = 0; j < 4; j++) {
					if (j == 0 || j == 3) {
						cb.setRGBColorStrokeF(255f, 0f, 0f);
					} else {
						cb.setRGBColorStrokeF(0f, 0f, 255f);
					}
					cb.moveTo(10 + lnmin, 820 - inmin);
					cb.lineTo(10 + 95 + lnmin, 820 - inmin);
					cb.stroke();

					inmin += 15;
				}
				lnmin += 95;
				cb.moveTo(10 + lnmin, 820 - min);
				cb.lineTo(10 + lnmin, (820 - 45 - min));
				cb.stroke();
				lnmin += 26;
				if (i == 5 || i == 11) {
					break;
				}
			}
			min += 68;
			cb.setRGBColorStrokeF(0f, 0f, 0f);
			if (i == 5) {
				cb.setLineWidth(2);
				cb.moveTo(-10, 820 - (min - 11));
				cb.lineTo(700, 820 - (min - 11));
				cb.stroke();
				cb.setLineWidth(1);
			}

		}
		cb.stroke();
	}

	public static void box(PdfContentByte cb) {
		cb.setLineWidth(1f);
		int min = 0;
		for (int i = 0; i < 15; i++) {
			cb.moveTo(10, 820 - min);
			cb.lineTo(580, 820 - min);
			cb.stroke();
			min += 57;
		}
		min = 0;
		for (int i = 0; i < 14; i++) {
			cb.moveTo(10 + min, 22);
			cb.lineTo(10 + min, 820);
			cb.stroke();
			min += 57;
		}
		cb.stroke();
	}

	public static void mathsBoxes(PdfContentByte cb) {
		cb.moveTo(10, 10);
		cb.lineTo(10, 820);
		cb.stroke();
		cb.moveTo(290, 10);
		cb.lineTo(290, 820);
		cb.stroke();
		cb.moveTo(580, 10);
		cb.lineTo(580, 820);
		cb.stroke();
		int min = 0;
		for (int i = 0; i < 9; i++) {
			cb.moveTo(10, 820 - min);
			cb.lineTo(580, 820 - min);
			cb.stroke();
			min += 101;
		}
	}

	public static void images(Document document, PdfContentByte cb) {
		int y = 820;
		int imageId = 1;
		for (int i = 0; i < 9; i++) {
			int x = 10;
			for (int k = 0; k < 2; k++) {
				System.out.println(i + " : " + k);
				addImages(x, y, document, cb, imageId++);
				imageId = (imageId == 9 ? 1 : imageId);
				x = 300;
				System.out.println("\n\n");
			}
			y = y - 100;
		}
	}

	public static void addImages(int x, int y, Document document, PdfContentByte cb, int imageId) {
		try {

			int tempx = x + 10;
			int tempy = y - 40;
			int sumNo = randomnum(5, 10);
			int first = randomnum(1, sumNo);
			if (first < 3)
				first = 3;

			if (sumNo == first)
				first = first - 2;
			int seco = sumNo - first;
			if (seco < 3) {
				seco = 3;
				first = sumNo - seco;
			}
			System.out.println("sumNo  : " + sumNo);
			System.out.println("first  : " + first);
			System.out.println("seco  : " + seco);

			for (int i = 0; i < first; i++) {
				Image img = getImage(imageId + "p");
				img.scaleToFit(20, 20);
				img.setAbsolutePosition(tempx, tempy);
				document.add(img);
				tempx = tempx + 20;
				if (i == 3 || i == 7) {
					tempy = tempy - 20;
					tempx = x + 10;
				}
			}
			createContent(cb, x + 110, y + 40, "+", 1, 30);
			cb.moveTo(x + 20, y - 90);
			cb.lineTo(x + 80, y - 90);
			cb.stroke();
			tempx = x + 120;
			tempy = y - 40;
			for (int i = 0; i < seco; i++) {
				Image img = getImage(imageId + "p");
				img.scaleToFit(20, 20);
				img.setAbsolutePosition(tempx, tempy);
				document.add(img);
				tempx = tempx + 20;
				if (i == 3 || i == 7) {
					tempy = tempy - 20;
					tempx = x + 120;
				}
			}
			cb.moveTo(x + 120, y - 90);
			cb.lineTo(x + 180, y - 90);
			cb.stroke();

			createContent(cb, x + 215, y + 40, "=", 1, 30);

			cb.moveTo(x + 230, y - 80);
			cb.lineTo(x + 280, y - 80);
			cb.stroke();

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void minusimages(Document document, PdfContentByte cb) {
		int y = 820;
		int imageId = 1;
		for (int i = 0; i < 9; i++) {
			int x = 10;
			for (int k = 0; k < 2; k++) {
				System.out.println(i + " : " + k);
				minusImages(x, y, document, cb, imageId++);
				imageId = (imageId == 9 ? 1 : imageId);
				x = 300;
				System.out.println("\n\n");
			}
			y = y - 100;
		}
	}

	public static void minusImages(int x, int y, Document document, PdfContentByte cb, int imageId) {
		try {

			int tempx = x + 10;
			int tempy = y - 40;
			int first = randomnum(3, 9);
			int seco = randomnum(1, first);
			if (first <= seco) {
				first = first + (seco - first) + 2;
			}

			System.out.println("first  : " + first);
			System.out.println("seco  : " + seco);

			for (int i = 0; i < first; i++) {
				Image img = getImage(imageId + "p");
				img.scaleToFit(20, 20);
				img.setAbsolutePosition(tempx, tempy);
				document.add(img);
				tempx = tempx + 20;
				if (i == 3 || i == 7) {
					tempy = tempy - 20;
					tempx = x + 10;
				}
			}
			createContent(cb, x + 110, y + 40, "-", 1, 30);
			cb.moveTo(x + 20, y - 90);
			cb.lineTo(x + 80, y - 90);
			cb.stroke();
			tempx = x + 120;
			tempy = y - 40;
			for (int i = 0; i < seco; i++) {
				Image img = getImage(imageId + "p");
				img.scaleToFit(20, 20);
				img.setAbsolutePosition(tempx, tempy);
				document.add(img);
				tempx = tempx + 20;
				if (i == 3 || i == 7) {
					tempy = tempy - 20;
					tempx = x + 120;
				}
			}
			cb.moveTo(x + 120, y - 90);
			cb.lineTo(x + 180, y - 90);
			cb.stroke();

			createContent(cb, x + 215, y + 40, "=", 1, 30);

			cb.moveTo(x + 230, y - 80);
			cb.lineTo(x + 280, y - 80);
			cb.stroke();

		} catch (Exception e) {
			// TODO: handle exception
		}

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

	public static int randomnum(int x, int y)// max value-y, min value-x
	{
		int z = (int) (Math.random() * ((y - x) + 1) + x); // Formula for random number generation within a range
		return z;
	}

	public static Image getImage(String name) {
		try {
			File resource = new File("D:\\Additions\\" + name + ".png");
			return Image.getInstance(resource.getAbsolutePath());
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}
}
