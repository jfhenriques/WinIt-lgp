package pt.techzebra.winit.client;

import java.io.Serializable;
import java.util.ArrayList;

public class TradingContainer implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public final ArrayList<Promotion> PROPOSABLE_PROMOTIONS;
    public final ArrayList<Proposal> RECEIVED_PROPOSALS;
    public final ArrayList<Proposal> SENT_PROPOSALS;

    public TradingContainer(ArrayList<Promotion> proposable_promotions,
            ArrayList<Proposal> received_proposals,
            ArrayList<Proposal> sent_proposals) {
        PROPOSABLE_PROMOTIONS = proposable_promotions;
        RECEIVED_PROPOSALS = received_proposals;
        SENT_PROPOSALS = sent_proposals;
    }
}
