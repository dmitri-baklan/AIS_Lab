package org.example.domain;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ITFBarcodePrinter {

    Map<Integer, List<Integer>> symbolCodes = new HashMap<>();

    int height = 100;  // Height of the rectangle
    int width = 1000;    // Width of the rectangle

    int startPoint = 80;

    int linePoint = 0;

    List<Integer> start = Arrays.asList(0,0);
    List<Integer> stop = Arrays.asList(1,0);
    int thickLine = 14;
    int line = 7;
    int y = 5;

    Graphics2D graphics;

    public ITFBarcodePrinter() {
        symbolCodes.put(0, Arrays.asList(0,0,1,1,0));
        symbolCodes.put(1, Arrays.asList(1,0,0,0,1));
        symbolCodes.put(2, Arrays.asList(0,1,0,0,1));
        symbolCodes.put(3, Arrays.asList(1,1,0,0,0));
        symbolCodes.put(4, Arrays.asList(0,0,1,0,1));
        symbolCodes.put(5, Arrays.asList(1,0,1,0,0));
        symbolCodes.put(6, Arrays.asList(0,1,1,0,0));
        symbolCodes.put(7, Arrays.asList(0,0,0,1,1));
        symbolCodes.put(8, Arrays.asList(1,0,0,1,0));
        symbolCodes.put(9, Arrays.asList(0,1,0,1,0));
    }

    public void printBarcode(List<Integer> numbers) {

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        graphics = image.createGraphics();

        // Set the background color (white)
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        ArrayList<Integer> digits = new ArrayList<>(numbers);
        if(isEvenNumberOfDigits(digits.size())) {
            digits.add(0, 0);
        }

        digits = addControlDigit(digits);


        setWidth(digits.size());

        setBarcode(digits);

        try {
            File output = new File("rectangle.png");
            ImageIO.write(image, "png", output);
            System.out.println("Vertical rectangle image saved to rectangle.png");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            graphics.dispose();
        }
    }

    private ArrayList<Integer> convertNumberToArray(int number) {
        String strNumber = Integer.toString(number);
        ArrayList<Integer> digitList = new ArrayList<>();
        for (int i = 0; i < strNumber.length(); i++) {
            digitList.add(Character.getNumericValue(strNumber.charAt(i)));
        }
        return digitList;
    }
    private boolean isEvenNumberOfDigits(int numberOfDigits) {
        return numberOfDigits % 2 == 0;
    }
    private void setWidth(int numberOfDigits) {
        width = numberOfDigits * thickLine * 2;
    }

    private void setBarcode(List<Integer> digits) {
        setStartLines();
        int j = 0;
        for(int i = 0; i < digits.size() - 1;) {
            j = i + 1;
            for(int k = 0; k < 5; k++) {
                setBlackLine(symbolCodes.get(digits.get(i)).get(k));
                setSpaceLine(symbolCodes.get(digits.get(j)).get(k));
            }
            i = i + 2;
        }
        setStopLines();
    }

    private void setStartLines() {
        linePoint = startPoint;
        for(int digit: start) {
            setBlackLine(digit);
            setSpaceLine(0);
        }
    }

    private void setStopLines() {
        for(int digit: stop) {
            setBlackLine(digit);
            setSpaceLine(0);
        }
    }

    private void setSpaceLine(int digit) {
        linePoint = linePoint + getWidth(digit);
    }

    private void setBlackLine(int digit) {
        graphics.setColor(Color.BLACK);
        setLine(digit);
    }

    private void setLine(int digit) {
        int width = getWidth(digit);

        int x  = linePoint;
        linePoint = linePoint + width;

        graphics.fillRect(x, y, width, height - 10);
    }

    private int getWidth(int digit) {
        if (digit == 0) {
            return line;
        }
        if (digit == 1) {
            return thickLine;
        }
        throw new RuntimeException();
    }

    private ArrayList<Integer> addControlDigit(ArrayList<Integer> digits) {
        int R1 = 0;
        int R2 = 0;
        for(int i = 0; i < digits.size(); i++) {
            if(i % 2 == 0) {
                R1 = R1 + digits.get(i);
            }
            if(i % 2 == 1) {
                R2 = R2 + digits.get(i);
            }
        }
        int sum = (R1*3+R2);
        int additional = (sum % 10);
        int C = 10 - additional;
        digits.add(C);
        return digits;
    }


}
