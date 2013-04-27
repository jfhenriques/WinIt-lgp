package pt.techzebra.promgamemobile.client;

public class Promotion {

    private String name_;
    private boolean active_;
    private String init_date_;
    private String end_date_;
    private boolean transferable_;
    private int win_points_;
    
    public Promotion(String name, boolean active, String init_date, String end_state, boolean transferable, int win_points){
        this.name_ = name;
        this.active_ = active;
        this.init_date_ = init_date;
        this.end_date_ = end_state;
        this.transferable_ = transferable;
        this.win_points_ = win_points;
    }

    public String getName_() {
        return name_;
    }

    public void setName_(String name_) {
        this.name_ = name_;
    }

    public boolean isActive_() {
        return active_;
    }

    public void setActive_(boolean active_) {
        this.active_ = active_;
    }

    public String getInit_date_() {
        return init_date_;
    }

    public void setInit_date_(String init_date_) {
        this.init_date_ = init_date_;
    }

    public String getEnd_date_() {
        return end_date_;
    }

    public void setEnd_date_(String end_date_) {
        this.end_date_ = end_date_;
    }

    public boolean isTransferable_() {
        return transferable_;
    }

    public void setTransferable_(boolean transferable_) {
        this.transferable_ = transferable_;
    }

    public int getWin_points_() {
        return win_points_;
    }

    public void setWin_points_(int win_points_) {
        this.win_points_ = win_points_;
    }
    
    
}
