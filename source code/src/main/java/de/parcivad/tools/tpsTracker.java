package de.parcivad.tools;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class tpsTracker implements Runnable {

    // needed for tps tracking
    public Integer tickTimeStopper = 0;
    public List<Float> tickRecorder = new ArrayList<Float>();
    public Integer recordStart = (int) System.currentTimeMillis();

    // needed for tps presenting and saving
    public static HashMap<Integer, Float> tpsRecords = new HashMap<Integer, Float>();
    public static HashMap<Integer, Float> memoryRecords = new HashMap<Integer, Float>();
    public static HashMap<Integer, Timestamp> recordIDAtTimestamp = new HashMap<Integer, Timestamp>();
    public Integer recordID = 0;

    @Override
    public void run() {

        // buffer each tpMs length into save
        int tickTime = ( (int) System.currentTimeMillis() - (int) this.tickTimeStopper ) / 100;
        float tickMs = 1000 / tickTime;
        // save in records
        this.tickRecorder.add(tickMs);
        // set tick length to now time
        this.tickTimeStopper = (int) System.currentTimeMillis();

        // get average tps
        int sum = 0;
        for ( int i = 0; i < tickRecorder.toArray().length; i++ ) {
            sum += this.tickRecorder.get(i);
        }
        // put average in tpsRecords
        float tps = (float) Math.round( (float) sum / ( this.tickRecorder.toArray().length  ) * 100 ) / 100;
        tpsRecords.put( this.recordID, tps );
        float memory = (float) Math.round( ( (float) Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() ) / 1000000000 * 100 ) / 100;
        memoryRecords.put( this.recordID, memory );

        // set new record on and reset average collection
        if ( 60000 <= this.tickTimeStopper - this.recordStart ) {
            // save Timestamp of this recordID
            recordIDAtTimestamp.put( this.recordID, new Timestamp(System.currentTimeMillis() ) );
            // reset collected tick length for average
            this.tickRecorder.clear();
            this.recordStart = (int) System.currentTimeMillis();
            // check if recording since an hour ( every id count == 1min)
            if ( this.recordID != 60 ) {
                // next slot
                this.recordID += 1;
            } else {
                // start overwriting
                tpsRecords.clear();
                memoryRecords.clear();
                recordIDAtTimestamp.clear();
                this.recordID = 0;
            }
        }
    }

}
