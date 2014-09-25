/*
 * What's been done:
 * 
 * 1. Verification: 
 * 		a. limited length of the text field
 * 		b. devision by 0
 * 		c. leading zero/ first zero
 * 		d. Handles situations such as 10 + 10 + 2 * 3... e.c.t
 * 		e. Handles num +- num - performs the last operation
 * 		f. Many more
 * 
 * 
 * 		
 *	TODO: remove single digit, sign flip
 *			
 */



package com.example.mycalculation;

// Imports

import java.text.DecimalFormat;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	TextView numdisplay, optxt;
	Button num_1, num_2, num_3, num_4, num_5, num_6, num_7, num_8, num_9, num_0, // all numeric value 
	op_add, op_sub, op_mul, op_eq, op_div,  // all basic operations
	op_clr, op_flip, op_dec, op_backsps;
	
	/*
	 * Variable initialization
	 */	
	double value1, value2;
	char performingOperation, prevOperation;
	char operation;
	String formattedOutput;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        numdisplay = (TextView)findViewById(R.id.numdisply);
		num_1 = (Button)findViewById(R.id.But1);
		num_2 = (Button)findViewById(R.id.But2);
		num_3 = (Button)findViewById(R.id.But3);
		num_4 = (Button)findViewById(R.id.But4);
		num_5 = (Button)findViewById(R.id.But5);
		num_6 = (Button)findViewById(R.id.But6);
		num_7 = (Button)findViewById(R.id.But7);
		num_8 = (Button)findViewById(R.id.But8);
		num_9 = (Button)findViewById(R.id.But9);
		num_0 = (Button)findViewById(R.id.But0);
		op_add = (Button)findViewById(R.id.Butadd);
		op_sub = (Button)findViewById(R.id.Butsub);
		op_mul = (Button)findViewById(R.id.Butmul);
		op_div = (Button)findViewById(R.id.Butdiv);
		op_eq = (Button)findViewById(R.id.Buteq);
		
		op_clr = (Button)findViewById(R.id.ButC);
		op_flip = (Button)findViewById(R.id.ButFlip);
		op_dec = (Button)findViewById(R.id.ButDec);
		op_backsps = (Button)findViewById(R.id.ButBck);
		
		// number handlers
		num_1.setOnClickListener(num_handler);
		num_2.setOnClickListener(num_handler);
		num_3.setOnClickListener(num_handler);
		num_4.setOnClickListener(num_handler);
		num_5.setOnClickListener(num_handler);
		num_6.setOnClickListener(num_handler);
		num_7.setOnClickListener(num_handler);
		num_8.setOnClickListener(num_handler);
		num_9.setOnClickListener(num_handler);
		num_0.setOnClickListener(num_handler);
		
		// operation handlers
		op_add.setOnClickListener(op_handler);
		op_sub.setOnClickListener(op_handler);
		op_mul.setOnClickListener(op_handler);
		op_div.setOnClickListener(op_handler);
		op_eq.setOnClickListener(op_handler);
		
		op_clr.setOnClickListener(op_handler);
		op_flip.setOnClickListener(op_handler);
		op_dec.setOnClickListener(op_handler);
		op_backsps.setOnClickListener(op_handler);
		
		// set the default values for all variables
		resetValues();
    }

    private OnClickListener num_handler = new OnClickListener() 
    { 
                public void onClick(View v) {
                	
                	// leading zeros and decimal point checks
                	if(numdisplay.getText().toString().charAt(0) == '0' && (!numdisplay.getText().toString().contains("."))){
                		numdisplay.setText("");
                	} 
                	
                	// clear input field 
                	if(value1 != 0 && value2 != 0){ 
                		resetValues();
                		numdisplay.setText("");
                	}
                	
                	if(verifyInput()){ // max length of field
                		Button B = (Button) v;
                		numdisplay.append(B.getText());
                	}
                		
                }
    };
    
    private OnClickListener op_handler = new OnClickListener() 
    { 
                public void onClick(View v) {
                	    
                	Button B = (Button) v;
                	operation = B.getText().toString().charAt(0); // performing operation  	
                	
                		switch(operation){ // determine which operation to perform
                			case 'C':
                				// clear all the values and the field
                				resetValues();
                				break;
                			case '«':
                				backspace();
                				break;
                			case '±':
                				numdisplay.setText(formatOutput(flipSign(getDoubleFromNum())));
                				if(value1 == flipSign(getDoubleFromNum()))
                					value1 = getDoubleFromNum();
                				else if(value2 == flipSign(getDoubleFromNum()))
                					value2 = getDoubleFromNum();
                				break;
                			case '.':
                				decimal();
                				break;
	                		case '/':
	                			performingOperation = '/';
	                			setValue();
	                			break;
	                		case '*':
	                			performingOperation = '*';
	                			setValue();
	                			break;
	                		case '+':
	                			performingOperation = '+';
	                			setValue();
	                			break;
	                		case '-':
	                			performingOperation = '-';
	                			setValue();
	                			break;		
	                		case '=':
	                			if(value2 == 0 && performingOperation != ' ')
	                				setValue();
	                			if(performingOperation != ' ' && verifyInput()){
	                				value1 = performOperation(performingOperation, value1, value2);
	                				formattedOutput = formatOutput(value1);
	                				numdisplay.setText(formattedOutput);
	                			}
	                			break;
	                			
	                		default:
	                			resetValues();
	                	}

                }		
    };
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /*
     * All the helper methods
     */
    
    public double flipSign(double arg){ // flips the sign of arg
    	if (arg != 0) 
    		return -(arg);
    	else 
    		return 0;
    }
    
    public void resetValues(){ // reset all the values
    	value1 = 0;
    	value2 = 0;
    	performingOperation = ' ';
    	prevOperation = ' ';
    	numdisplay.setText("0");
    }
    
    public double getDoubleFromNum(){ // get double from numeric field
    	return Double.valueOf(numdisplay.getText().toString());
    }
    
    public void setValue(){ // sets value from numeric field
    	
    	if(value1 == 0){
    		value1 = getDoubleFromNum();
    		numdisplay.setText("0");
    	} else if(value2 == 0){
    		value2 = getDoubleFromNum();
    		if(operation != '=')
    		{
    			value1 = performOperation(prevOperation, value1, value2);
    			value2 = 0;
    		} 
    		
    		numdisplay.setText("0");
    		
    			
    	} else {
    		// value1 = performOperation(performingOperation, value1, value2);
    		value2 = 0;
    		numdisplay.setText("0");
    	}
    	
    	prevOperation = performingOperation;
    }
    
    public boolean verifyInput(){ // verifies numfield for max length
    	if(numdisplay.getText().toString().length() < 15)
    		return true;
    	else {
    		Toast.makeText(getBaseContext(), "You reached max input size", Toast.LENGTH_SHORT).show();
    		return false;
    	}
    		
    }
    
    public double performOperation(char operation, double val1, double val2){ // perform operation on val1 & val2
    	
    	switch(operation){
    	
    	case '*': 
    		return val1 * val2;
    	case '/':
    		if(val2 == 0){
    			Toast.makeText(getApplicationContext(), "Error diving by zero", Toast.LENGTH_SHORT).show();
    			resetValues();
    			return 0;
    		} 
    		else 
    			return val1 / val2;
    	case '+':
    		return val1 + val2;
    	case '-':
    		return val1 - val2;
    	default:
    		return 0;
    	}
    }
    
    public String formatOutput(double arg){ // formats output
    	
    	if((arg == Math.floor(arg)) && !Double.isInfinite(arg)){
    		DecimalFormat df = new DecimalFormat("#");
    		return df.format(arg);
    	} 
    	
    	if(String.valueOf(arg).length() > 0)
    	{	
    		char prev = String.valueOf(arg).charAt(0);
    		int length = String.valueOf(arg).length();
    		for (int i = 1; i < length; i++) {
				if(prev == String.valueOf(arg).charAt(i))
					return String.valueOf(arg).substring(0, i);
				prev = String.valueOf(arg).charAt(i);
			}
    	}
    		return String.valueOf(arg);
    }
    
    public void decimal(){ // handle decimal functionality
    	String val = numdisplay.getText().toString();
    	if(!val.contains(".")){
    		numdisplay.setText(formatOutput(getDoubleFromNum()) + ".");
    	}
    }
    
    public void backspace(){ // backspace key
    	String text = numdisplay.getText().toString();
    	if (text.length() > 1 && verifyInput()){
    		numdisplay.setText(text.substring(0, text.length()-1));
    		if(value2 != 0)
    			value1 = getDoubleFromNum();
    	}
    	else 
    		numdisplay.setText("0");
    	
    }
}
