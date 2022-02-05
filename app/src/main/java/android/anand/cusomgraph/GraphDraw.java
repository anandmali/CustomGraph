package android.anand.cusomgraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class GraphDraw extends View {

    private static final int MIN_LINES = 3;
    private static final int MAX_LINES = 9;
    private static final int[] DISTANCES = {1, 2, 5};

    private float[] pointValues = new float[]{};
    private final Paint paint = new Paint();

    public GraphDraw(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Method called to create chart
     */
    public void setChartData(float[] datapoints) {
        this.pointValues = datapoints.clone();
        invalidate();
    }

    /**
     * --- onDraw method
     * Called to draw graph components in canvas using onDraw method
     */
    @Override
    protected void onDraw(Canvas canvas) {

        //Draw graph background
        GraphBackground(canvas);

        //Draw line of graph representation
        DrawGraphLine(canvas);

        //Draw circles at each point of deviation for Y-axis
        drawCircleDot(canvas);

        //Draw lable for points of Y-axis
        drawPointLable(canvas);
    }

    /**
     * Draw background of the graph
     */
    private void GraphBackground(Canvas canvas) {
        float maxValue = getMax_Value(pointValues);
        int range = LineDistance(maxValue);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        for (int y = 0; y < maxValue; y += range) {
            final float yPos = getYPosition(y);

            //Draws lines in background
            canvas.drawLine(0, yPos, getWidth(), yPos, paint);

            //Draws text to the lines drawn
            canvas.drawText(String.valueOf(y), getPaddingLeft(), yPos - 2, paint);

        }
    }

    /**
     * Measure the distance between horizontal lines for graph background
     */
    private int LineDistance(float maxValue) {
        int distance;
        int distanceIndex = 0;
        int distanceMultiplier = 1;
        int numberOfLines;

        do {
            distance = DISTANCES[distanceIndex] * distanceMultiplier;
            numberOfLines = (int) Math.ceil(maxValue / distance);

            distanceIndex++;
            if (distanceIndex == DISTANCES.length) {
                distanceIndex = 0;
                distanceMultiplier *= 10;
            }
        } while (numberOfLines < MIN_LINES || numberOfLines > MAX_LINES);

        return distance;
    }

    /**
     * Draw the main graph lines, that shows the path of data points change
     */
    private void DrawGraphLine(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getXPosition(0), getYPosition(pointValues[0]));
        for (int i = 1; i < pointValues.length; i++) {
            path.lineTo(getXPosition(i), getYPosition(pointValues[i]));
        }

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        paint.setShadowLayer(2, 1, 1, 0x80000000);
        canvas.drawPath(path, paint);
    }

    /**
     * Get the maximum value of the array of data points given for Y-axis
     */
    private float getMax_Value(float[] array) {
        float max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    /**
     * Gives back the position of point according to Y-axis
     */
    private float getYPosition(float value) {
        float height = getHeight() - getPaddingTop() - getPaddingBottom();
        float maxValue = getMax_Value(pointValues);

        // scale it to the view size
        value = (value / maxValue) * height;

        // invert it so that higher values have lower y
        value = height - value;

        // offset it to adjust for padding
        value += getPaddingTop();

        return value;
    }

    /**
     * Gives back the position of point according to X-axis
     */
    private float getXPosition(float value) {
        float width = getWidth() - getPaddingLeft() - getPaddingRight();
        float maxValue = pointValues.length - 1;

        // scale it to the view size
        value = (value / maxValue) * width;

        // offset it to adjust for padding
        value += getPaddingLeft();

        return value;
    }

    /**
     * Draw circles at each deviations occurred byt data points given for Y-axis
     */
    private void drawCircleDot(Canvas canvas) {
        for (int i = 0; i < pointValues.length; i++) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.RED);
            canvas.drawText("point " + (i + 1), getXPosition(i) + 4, getYPosition(pointValues[i]), paint);
            canvas.drawCircle(getXPosition(i), getYPosition(pointValues[i]), 5, paint);
        }
    }

    /**
     * Label the points at each deviation occurred by data points given for Y-axis
     */
    private void drawPointLable(Canvas canvas) {
        for (int i = 0; i < pointValues.length; i++) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.YELLOW);
            canvas.drawText("point " + i, getXPosition(i) + 4, getYPosition(pointValues[i]), paint);
        }
    }
}
