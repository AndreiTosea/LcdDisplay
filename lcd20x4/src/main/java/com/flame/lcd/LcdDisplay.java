package com.flame.lcd;

import com.pi4j.component.lcd.LCD;
import com.pi4j.component.lcd.LCDBase;
import com.pi4j.io.gpio.Pin;
import com.pi4j.wiringpi.Lcd;


/**
 * LCD Display for PI4G
 * Control the LCD display with 2 and 4 rows
 */
public class LcdDisplay extends LCDBase implements LCD {
    protected int rows;
    protected int columns;
    private int lcdHandle;

    public LcdDisplay(int rows, int columns, Pin rsPin, Pin strobePin, Pin... dataPins) {
        this.rows = rows;
        this.columns = columns;
        int[] bits = new int[]{0, 0, 0, 0, 0, 0, 0, 0};

        for(int index = 0; index < 8; ++index) {
            if (index < dataPins.length) {
                bits[index] = dataPins[index].getAddress();
            }
        }

        this.lcdHandle = Lcd.lcdInit(rows, columns, dataPins.length, rsPin.getAddress(), strobePin.getAddress(), bits[0], bits[1], bits[2], bits[3], bits[4], bits[5], bits[6], bits[7]);
        if (this.lcdHandle == -1) {
            throw new RuntimeException("Invalid LCD handle returned from wiringPi: " + this.lcdHandle);
        }
    }

    public int getRowCount() {
        return this.rows;
    }

    public int getColumnCount() {
        return this.columns;
    }

    public void clear() {
        Lcd.lcdClear(this.lcdHandle);
    }

    public void setCursorHome() {
        Lcd.lcdHome(this.lcdHandle);
    }

    public void setCursorPosition(int row, int column) {
        this.validateCoordinates(row, column);
        Lcd.lcdPosition(this.lcdHandle, column, row);
    }

    public void write(byte data) {
        Lcd.lcdPutchar(this.lcdHandle, data);
    }

    public void write(String data) {
        Lcd.lcdPuts(this.lcdHandle, data);
    }
}
