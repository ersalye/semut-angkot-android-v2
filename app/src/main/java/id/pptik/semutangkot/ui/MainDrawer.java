package id.pptik.semutangkot.ui;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.GravityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mikepenz.crossfadedrawerlayout.view.CrossfadeDrawerLayout;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.interfaces.ICrossfader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;

import id.pptik.semutangkot.R;

public class MainDrawer {

    private Drawer result = null;
    private MiniDrawer miniResult = null;
    private CrossfadeDrawerLayout crossfadeDrawerLayout = null;

    public void attach(Activity context){

        result = new DrawerBuilder()
                .withActivity(context)
                .withDrawerLayout(R.layout.crossfade_material_drawer)
                .withHasStableIds(true)
                .withDrawerWidthDp(72)
                .withTranslucentStatusBar(false)
                .withDisplayBelowStatusBar(false)
                .withGenerateMiniDrawer(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Map").withIcon(GoogleMaterial.Icon.gmd_map).withIdentifier(1),
                        new PrimaryDrawerItem().withName("Cek KIR").withIcon(GoogleMaterial.Icon.gmd_rv_hookup).withIdentifier(2),
                        new SectionDrawerItem().withName("Account"),
                        new SecondaryDrawerItem().withName("Logout").withIcon(FontAwesome.Icon.faw_sign_out)
                )
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    if (drawerItem instanceof Nameable) {
                        Toast.makeText(context, ((Nameable) drawerItem).getName().getText(context), Toast.LENGTH_SHORT).show();
                    }
                    return false;
                })
                .withShowDrawerOnFirstLaunch(true)
                .build();

        crossfadeDrawerLayout = (CrossfadeDrawerLayout) result.getDrawerLayout();
        crossfadeDrawerLayout.setMaxWidthPx(DrawerUIUtils.getOptimalDrawerWidth(context));

        miniResult = result.getMiniDrawer();

        View view = miniResult.build(context);
        view.setBackgroundColor(context.getResources().getColor(R.color.primary_dark));
        crossfadeDrawerLayout.getSmallView().addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        miniResult.withCrossFader(new ICrossfader() {
            @Override
            public void crossfade() {
                crossfadeDrawerLayout.crossfade(400);
                //only close the drawer if we were already faded and want to close it now
                if (isCrossfaded()) {
                    result.getDrawerLayout().closeDrawer(GravityCompat.START);
                }
            }

            @Override
            public boolean isCrossfaded() {
                return crossfadeDrawerLayout.isCrossfaded();
            }
        });
    }



    public MiniDrawer getMiniDrawer(){
        return miniResult;
    }
}
