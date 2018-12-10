package com.example.prince.camruler;



import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prince.camruler.R;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



public class Image extends MainActivity {
    Button btnCamera;
    Button btnClear;
    Button btnOK;
    TextView ob1txt;
    TextView ob2txt;
    TextView txt2;
    FrameLayout imageholder;
    ImageView iv;
    ImageView iv1;
    File photoFile = null;
    DrawingOnImage drawing;
    double[] result;
    int outputUnit;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);

        btnCamera = (Button) findViewById(R.id.takePicture);
        btnClear = (Button) findViewById(R.id.clear);
        btnClear.setVisibility(View.INVISIBLE);
        btnOK = (Button) findViewById(R.id.ok);
        btnOK.setVisibility(View.INVISIBLE);
        txt2 = (TextView) findViewById(R.id.textView2);
        txt2.setVisibility(View.INVISIBLE);
        ob1txt = (TextView) findViewById(R.id.object1Text);
        ob1txt.setVisibility(View.INVISIBLE);
        ob2txt = (TextView) findViewById(R.id.objects2Text);
        ob2txt.setVisibility(View.INVISIBLE);
        imageholder = (FrameLayout) findViewById(R.id.frame);
        iv = (ImageView) findViewById(R.id.ruler);
        iv1 = (ImageView) findViewById(R.id.ruler2);



        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                runTakePicture();
                buttonDisappear();

            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawing.circlePoints.size() !=0){

                    drawing.clearCanvas();
                }
                if (drawing.circlePoints.size() > 0 && drawing.circlePoints.size() <= 2){

                    txt2.setVisibility(View.VISIBLE);
                    ob1txt.setVisibility(View.INVISIBLE);
                    ob2txt.setVisibility(View.INVISIBLE);
                }
                else if (drawing.circlePoints.size() > 2 && drawing.circlePoints.size() <= 4){

                    txt2.setVisibility(View.INVISIBLE);
                    ob1txt.setVisibility(View.VISIBLE);
                    ob2txt.setVisibility(View.INVISIBLE);
                }
                else if (drawing.circlePoints.size() > 4 && drawing.circlePoints.size() <=6){

                    txt2.setVisibility(View.INVISIBLE);
                    ob1txt.setVisibility(View.INVISIBLE);
                    ob2txt.setVisibility(View.VISIBLE);
                }

            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean objects1 = false;

                if (drawing.circlePoints.size() == 2) {
                    drawing.nums = 4;
                    //objects1 = true;
                    Log.i("size 2 select  1", "in size 2 sleted 1");


                }
                if (drawing.circlePoints.size() == 4 && MainActivity.objectsCount.equals(("1"))) {
                    Log.i("in ize 4 select  2", "in size 2 sleted 2");

                    objects1 = true;


                }
                if (drawing.circlePoints.size() == 4 && MainActivity.objectsCount.equals(("2"))) {
                    Log.i("in ize 4 select  2", "in size 2 sleted 2");
                    drawing.nums = 6;


                }
                if (drawing.circlePoints.size() == 6) {
                    objects1 = true;
                }
                if (drawing.circlePoints.size() > 3 && objects1) {
                    LayoutInflater li = LayoutInflater.from(context);

                    final View promptsView = li.inflate(R.layout.dialog, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    alertDialogBuilder.setView(promptsView);


                    alertDialogBuilder.setTitle("Reference Object Measurements");
                    alertDialogBuilder.setIcon(R.drawable.new_logo);

                    alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            int inputUnit = ((Spinner) promptsView.findViewById(R.id.input_unit_chooser)).getSelectedItemPosition();
                            outputUnit = ((Spinner) promptsView.findViewById(R.id.output_unit_chooser)).getSelectedItemPosition();

                            try {
                                double reference = Double.parseDouble(((EditText) promptsView.findViewById(R.id.reference_input)).getText().toString());
                                result = drawing.calculate(reference, inputUnit, outputUnit);
                                showResult();
                            } catch (NumberFormatException ex) {
                                Toast.makeText(Image.this, getResources().getString(R.string.error_numberFormat), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //Put actions for CANCEL button here, or leave in blank
                        }
                    });

                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    final Spinner mSpinner = (Spinner) promptsView
                            .findViewById(R.id.input_unit_chooser);


                    alertDialog.show();
                    alertDialog.setCanceledOnTouchOutside(true);
                } else if ((drawing.circlePoints.size() == 3 && MainActivity.objectsCount.equals("1"))) {
                    Toast.makeText(Image.this, "Please draw all dots first", Toast.LENGTH_SHORT).show();

                } else if ((drawing.circlePoints.size() == 1 && MainActivity.objectsCount.equals("1"))) {
                    Toast.makeText(Image.this, "Please draw all dots first", Toast.LENGTH_SHORT).show();
                } else if ((drawing.circlePoints.size() == 5 && MainActivity.objectsCount.equals("2"))) {
                    Toast.makeText(Image.this, "Please draw all dots first", Toast.LENGTH_SHORT).show();
                } else if ((drawing.circlePoints.size() == 3 && MainActivity.objectsCount.equals("2"))) {
                    Toast.makeText(Image.this, "Please draw all dots first", Toast.LENGTH_SHORT).show();
                }else if ((drawing.circlePoints.size() == 1 && MainActivity.objectsCount.equals("2"))) {
                    Toast.makeText(Image.this, "Please draw all dots first", Toast.LENGTH_SHORT).show();
                }
                if (drawing.circlePoints.size() == 2 ){
                    txt2.setVisibility(View.INVISIBLE);
                    ob1txt.setVisibility(View.VISIBLE);
                    ob2txt.setVisibility(View.INVISIBLE);
                }
                else if (drawing.circlePoints.size() == 4 && objects1 == false){
                    ob1txt.setVisibility(View.INVISIBLE);
                    ob2txt.setVisibility(View.VISIBLE);
                }
                else if (drawing.circlePoints.size() == 4 && objects1 == true){
                    ob1txt.setVisibility(View.VISIBLE);
                    ob2txt.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SurfaceImage image = new SurfaceImage(this, photoFile);
        imageholder.addView(image);
        drawing = new DrawingOnImage(this, 2);
        imageholder.addView(drawing);

        txt2.setVisibility(View.VISIBLE);
        btnOK.setVisibility(View.VISIBLE);
        btnClear.setVisibility(View.VISIBLE);

    }

    private void buttonDisappear() {

        btnCamera.setVisibility(View.INVISIBLE);
        iv.setVisibility(View.INVISIBLE);
        iv1.setVisibility(View.INVISIBLE);
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void runTakePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Context context = getApplicationContext();
                CharSequence text = "Error occurred while creating the File";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                //toast.show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(Image.this,"com.example.prince.provider",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 1);
            }
        }

    }

    private void showResult(){
        LayoutInflater li = LayoutInflater.from(context);
        final View promptsView = li.inflate(R.layout.dialog, null);
        String unit = ((Spinner) promptsView.findViewById(R.id.output_unit_chooser)).getItemAtPosition(outputUnit).toString();
        if(result[0] != -1) {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            if (MainActivity.objectsCount.equals("2")){
                Log.i("selected ","2");
                //builder.setMessage(getResources().getString(R.string.result_lbl) + decimalFormat.format(result[1]));
                builder.setMessage("Length of Red Object: " + decimalFormat.format(result[0]) + " "+unit+"\n" +"\n"+
                        "Length of Blue Object: " + decimalFormat.format(result[1])+" "+unit);
                builder.setPositiveButton("Done", null);

            }
            else{
                builder.setMessage("Length: " + decimalFormat.format(result[0]) + " " + unit);
                builder.setPositiveButton("Done", null);

            }
            builder.create().show();
        }
    }



}
