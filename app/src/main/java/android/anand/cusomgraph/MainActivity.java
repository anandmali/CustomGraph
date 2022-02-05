package android.anand.cusomgraph;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get custom graph view
        GraphDraw graphDraw = (GraphDraw) findViewById(R.id.graph);

        //Initiate graph with new data
        graphDraw.setChartData(GraphValues());

    }

    /**
     * Array to hold data fpr graph
     */
    public float[] GraphValues() {
        return new float[]{3, 5, 4, 8, 5, 7, 4};
    }

}
