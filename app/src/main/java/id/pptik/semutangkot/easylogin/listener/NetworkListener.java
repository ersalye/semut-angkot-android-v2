package id.pptik.semutangkot.easylogin.listener;

import id.pptik.semutangkot.easylogin.networks.SocialNetwork;

/**
 * Created by maksim on 14.02.16.
 */
interface NetworkListener {

    void onError(SocialNetwork.Network socialNetwork, String errorMessage);
}
