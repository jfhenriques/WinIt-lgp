package pt.techzebra.winit.client;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Promotion implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int promotion_id_;
    private String name_;
    private String description_;
    private String image_url_;
    private int active_; // 0 - not active | 1 - active
    private long init_date_;
    private long end_date_;
    private int grand_limit_;
    private int user_limit_;
    // private String valid_coord_;
    // private int valid_coord_radius_;
    private int win_points_;
    private int func_type_;
    private int retailer_id_;
    private int promotion_type_id_;
    private boolean transferable_;
    private int max_util_date_;
    private int pcid;
    private int active_upid_;
    
    private String code_;

    public Promotion(int pid, String name, String image_url) {
        promotion_id_ = pid;
        name_ = name;
        image_url_ = image_url;
    }

    public Promotion(int pid, int pcid, String name, int max_util_date,
            String image) {
        this.promotion_id_ = pid;
        this.pcid = pcid;
        this.name_ = name;
        this.max_util_date_ = max_util_date;
        this.image_url_ = image;
    }

    public Promotion(int pid, String name, String description,
            String image_url, long init_date, long end_date, int user_limit,
            boolean transferable, int win_points, int retailer_id,
            int promotion_type_id, int max_util_date, int active_upid, int pc_id, String code) {
        promotion_id_ = pid;
        name_ = name;
        description_ = description;
        image_url_ = image_url;
        // active_= active;
        init_date_ = init_date;
        end_date_ = end_date;
        user_limit_ = user_limit;
        win_points_ = win_points;
        retailer_id_ = retailer_id;
        promotion_type_id_ = promotion_type_id;
        transferable_ = transferable;
        max_util_date_ = max_util_date;
        active_upid_ = active_upid;
        pcid = pc_id;
        code_ = code;
    }

    public int getPcid() {
        return pcid;
    }
    
    public String getCode() {
        return code_;
    }

    public void setPcid(int pcid) {
        this.pcid = pcid;
    }

    public int getPromotionID() {
        return promotion_id_;
    }

    public String getName() {
        return name_;
    }

    public int getActive() {
        return active_;
    }

    public void setActive(int active_) {
        this.active_ = active_;
    }

    public int getGrandLimit() {
        return grand_limit_;
    }

    public void setGrandLimit(int grand_limit_) {
        this.grand_limit_ = grand_limit_;
    }

    public int getUserLimit() {
        return user_limit_;
    }

    public void setUserLimit(int user_limit_) {
        this.user_limit_ = user_limit_;
    }

    public int getWinPoints() {
        return win_points_;
    }

    public void setWinPoints(int win_points_) {
        this.win_points_ = win_points_;
    }

    public boolean getTransferable() {
        return transferable_;
    }

    public void setTransferable(boolean transferable_) {
        this.transferable_ = transferable_;
    }

    public String getDescription() {
        return description_;
    }

    public String getImageUrl() {
        return image_url_;
    }

    public void setImageUrl(String url) {
        this.image_url_ = url;
    }

    public long getInitDate() {
        return init_date_;
    }

    public long getEndDate() {
        return end_date_;
    }

    public int getFuncType() {
        return func_type_;
    }

    public int getRetailerId() {
        return retailer_id_;
    }

    public int getPromotionTypeId() {
        return promotion_type_id_;
    }

    public int getActiveUPID() {
        return active_upid_;
    }

    public void setActiveUPID(int active_upid_) {
        this.active_upid_ = active_upid_;
    }

    /**
     * Creates and returns an instance of the promotion from the provided JSON
     * data.
     * 
     * @param promotion
     *            The JSONObject containing promotion data
     * @return promotion The new instance of promotion created from the JSON
     *         data
     */
    public static Promotion valueOf(JSONObject promotion) {
        final int promotion_id = promotion.optInt("pid");
        final String name = promotion.optString("name");
        final String description = promotion.optString("desc");
        final String image_url = promotion.optString("image");

        final long init_date = promotion.optLong("init_date");
        final long end_date = promotion.optLong("end_date");
        final int max_util_date = promotion.optInt("max_util_date");
        final int user_limit = promotion.optInt("user_limit");
        final int win_points = promotion.optInt("win_points");
        final int retailer_id = promotion.optInt("rid");
        final int promotion_type_id = promotion.optInt("ptid");
        final boolean transferable = promotion.optBoolean("transferable");
        final int active_upid = promotion.isNull("active_upid") ? -1
                : promotion.optInt("active_upid");
        final int pcid = promotion.optInt("pcid");

        final String code = promotion.optString("code");
        
        return new Promotion(promotion_id, name, description, image_url,
                init_date, end_date, user_limit, transferable, win_points,
                retailer_id, promotion_type_id, max_util_date, active_upid,
                pcid, code);
    }

    public static Promotion valueOfTrading(JSONObject promotion) {
        final int promotion_id = promotion.optInt("pid");
        final String name = promotion.optString("name");
        final int promotion_code_id = promotion.optInt("pcid");
        final int max_util_date = promotion.optInt("max_util_date");
        final String image_url = promotion.optString("image");

        return new Promotion(promotion_id, promotion_code_id, name,
                max_util_date, image_url);
    }
}
