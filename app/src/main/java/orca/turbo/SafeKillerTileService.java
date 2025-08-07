package orca.turbo;

import android.os.Build;
import android.service.quicksettings.TileService;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.DataOutputStream;

@RequiresApi(api = Build.VERSION_CODES.N)
public class SafeKillerTileService extends TileService {

    @Override
    public void onClick() {
        unlockAndRun(() -> new Thread(() -> {
            try {
                Process p = Runtime.getRuntime().exec("su");
                DataOutputStream os = new DataOutputStream(p.getOutputStream());

                // Grant POST_NOTIFICATIONS runtime permission
                os.writeBytes("pm grant " + getPackageName() + " android.permission.POST_NOTIFICATIONS\n");

                // Force enable notifications using appops and settings
                os.writeBytes("cmd appops set " + getPackageName() + " POST_NOTIFICATION allow\n");
                os.writeBytes("settings put secure enabled_notification_list $(settings get secure enabled_notification_list)," + getPackageName() + "\n");

                // Kill user-installed apps except for the specified games
                os.writeBytes("for pkg in $(pm list packages -3 | cut -d':' -f2); do\n");
                os.writeBytes("  if [ \"$pkg\" != \"com.proximabeta.mf.uamo\" ] && [ \"$pkg\" != \"com.mobile.legends\" ] && [ \"$pkg\" != \"com.tencent.KiHan\" ] && [ \"$pkg\" != \"com.tencent.tmgp.cf\" ] && [ \"$pkg\" != \"com.tencent.tmgp.cod\" ] && [ \"$pkg\" != \"com.tencent.tmgp.gnyx\" ] && [ \"$pkg\" != \"com.ea.gp.apexlegendsmobilefps\" ] && [ \"$pkg\" != \"com.levelinfinite.hotta.gp\" ] && [ \"$pkg\" != \"com.supercell.clashofclans\" ] && [ \"$pkg\" != \"com.vng.mlbbvn\" ] && [ \"$pkg\" != \"com.levelinfinite.sgameGlobal\" ] && [ \"$pkg\" != \"com.tencent.tmgp.sgame\" ] && [ \"$pkg\" != \"com.pubg.krmobile\" ] && [ \"$pkg\" != \"com.rekoo.pubgm\" ] && [ \"$pkg\" != \"com.tencent.tmgp.pubgmhd\" ] && [ \"$pkg\" != \"com.vng.pubgmobile\" ] && [ \"$pkg\" != \"com.pubg.imobile\" ] && [ \"$pkg\" != \"com.tencent.ig\" ] && [ \"$pkg\" != \"com.YoStar.AetherGazer\" ] && [ \"$pkg\" != \"com.netease.lztgglobal\" ] && [ \"$pkg\" != \"com.riotgames.league.wildrift\" ] && [ \"$pkg\" != \"com.riotgames.league.wildrifttw\" ] && [ \"$pkg\" != \"com.riotgames.league.wildriftvn\" ] && [ \"$pkg\" != \"com.epicgames.fortnite\" ] && [ \"$pkg\" != \"com.epicgames.portal\" ] && [ \"$pkg\" != \"com.tencent.lolm\" ] && [ \"$pkg\" != \"jp.konami.pesam\" ] && [ \"$pkg\" != \"com.dts.freefiremax\" ] && [ \"$pkg\" != \"com.dts.freefireth\" ] && [ \"$pkg\" != \"com.ea.gp.fifamobile\" ] && [ \"$pkg\" != \"com.pearlabyss.blackdesertm.gl\" ] && [ \"$pkg\" != \"com.pearlabyss.blackdesertm\" ] && [ \"$pkg\" != \"com.activision.callofduty.shooter\" ] && [ \"$pkg\" != \"com.gameloft.android.ANMP.GloftA9HM\" ] && [ \"$pkg\" != \"com.madfingergames.legends\" ] && [ \"$pkg\" != \"com.riotgames.league.teamfighttactics\" ] && [ \"$pkg\" != \"com.riotgames.league.teamfighttacticstw\" ] && [ \"$pkg\" != \"com.riotgames.league.teamfighttacticsvn\" ] && [ \"$pkg\" != \"com.garena.game.codm\" ] && [ \"$pkg\" != \"com.tencent.tmgp.kr.codm\" ] && [ \"$pkg\" != \"com.vng.codmvn\" ] && [ \"$pkg\" != \"com.mobilelegends.mi\" ] && [ \"$pkg\" != \"com.supercell.brawlstars\" ] && [ \"$pkg\" != \"com.blizzard.diablo.immortal\" ] && [ \"$pkg\" != \"com.netease.newspike\" ] && [ \"$pkg\" != \"com.activision.callofduty.warzone\" ] && [ \"$pkg\" != \"com.pubg.newstate\" ] && [ \"$pkg\" != \"com.gamedevltd.destinywarfare\" ] && [ \"$pkg\" != \"com.pikpok.dr2.play\" ] && [ \"$pkg\" != \"com.CarXTech.highWay\" ] && [ \"$pkg\" != \"com.nekki.shadowfight3\" ] && [ \"$pkg\" != \"com.nekki.shadowfightarena\" ] && [ \"$pkg\" != \"com.gameloft.android.ANMP.GloftA8HM\" ] && [ \"$pkg\" != \"com.nekki.shadowfight\" ] && [ \"$pkg\" != \"com.ea.game.nfs14_row\" ] && [ \"$pkg\" != \"com.ea.games.r3_row\" ] && [ \"$pkg\" != \"com.supercell.squad\" ] && [ \"$pkg\" != \"com.blitzteam.battleprime\" ] && [ \"$pkg\" != \"com.garena.game.df\" ] && [ \"$pkg\" != \"com.proxima.dfm\" ]; then\n");
                os.writeBytes("    am force-stop \"$pkg\"\n");
                os.writeBytes("    killall -9 \"$pkg\" 2>/dev/null\n");
                os.writeBytes("  fi\n");
                os.writeBytes("done\n");

                // Boost BGMI
                os.writeBytes("am set-foreground-service com.pubg.imobile 1\n");
                os.writeBytes("dumpsys deviceidle whitelist +com.pubg.imobile\n");

                os.writeBytes("exit\n");
                os.flush();
                os.close();

                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(SafeKillerTileService.this, "Turbo Activated", Toast.LENGTH_SHORT).show());

            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(SafeKillerTileService.this, "Root required!", Toast.LENGTH_SHORT).show());
            }
        }).start());
    }
}
