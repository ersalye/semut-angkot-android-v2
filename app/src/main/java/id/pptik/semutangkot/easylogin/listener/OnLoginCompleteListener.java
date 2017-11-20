package id.pptik.semutangkot.easylogin.listener;

import id.pptik.semutangkot.easylogin.networks.SocialNetwork;

public interface OnLoginCompleteListener extends NetworkListener {
    /**
     * Called when login complete.
     * @param network id of social network where request was complete
     */
    void onLoginSuccess(SocialNetwork.Network network);
}