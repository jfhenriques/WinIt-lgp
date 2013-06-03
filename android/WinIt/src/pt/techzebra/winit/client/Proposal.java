package pt.techzebra.winit.client;

import org.json.JSONObject;

public class Proposal {
    private int my_pcid_;
    private int my_pid_;
    private long my_max_util_date_;
    private String my_name_;
    private String my_image_;
    
    private int want_pcid_;
    private int want_pid_;
    private long want_max_util_date_;
    private String want_name_;
    private String want_image_;
    
    public Proposal(int my_pcid, int my_pid, long my_max_util_date,
            String my_name, String my_image, int want_pcid, int want_pid,
            long want_max_util_date, String want_name, String want_image) {
        my_pcid_ = my_pcid;
        my_pid_ = my_pid;
        my_max_util_date_ = my_max_util_date;
        my_name_ = my_name;
        my_image_ = my_image;
        
        want_pcid_ = want_pcid;
        want_pid_ = want_pid;
        want_max_util_date_ = want_max_util_date;
        want_name_ = want_name;
        want_image_ = want_image;
    }
    
    public static Proposal valueOf(JSONObject proposal) {
        int my_pcid = proposal.optInt("pcid_my");
        int my_pid = proposal.optInt("pid_my");
        long my_max_util_date = proposal.optLong("max_util_date_my");
        String my_name = proposal.optString("name_my");
        String my_image = proposal.optString("image_my");
        
        int want_pcid = proposal.optInt("pcid_want");
        int want_pid = proposal.optInt("pid_want");
        long want_max_util_date = proposal.optLong("max_util_date_want");
        String want_name = proposal.optString("name_want");
        String want_image = proposal.optString("image_want");
        
        return new Proposal(my_pcid, my_pid, my_max_util_date, my_name,
                my_image, want_pcid, want_pid, want_max_util_date, want_name,
                want_image);
    }
}
