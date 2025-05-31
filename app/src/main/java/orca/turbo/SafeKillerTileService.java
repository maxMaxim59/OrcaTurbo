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

                // Kill user-installed apps except BGMI and MT
                os.writeBytes("for pkg in $(pm list packages -3 | cut -d':' -f2); do\n");
                os.writeBytes("  if [ \"$pkg\" != \"com.pubg.imobile\" ] && [ \"$pkg\" != \"com.tencent.ig\" ] && [ \"$pkg\" != \"com.pubg.krmobile\" ] && [ \"$pkg\" != \"com.vng.pubgmobile\" ] && [ \"$pkg\" != \"com.rekoo.pubgm\" ] && [ \"$pkg\" != \"com.pubg.newstate\" ] && [ \"$pkg\" != \"com.tencent.iglite\" ] && [ \"$pkg\" != \"com.dts.freefireth\" ] && [ \"$pkg\" != \"com.dts.freefiremax\" ] && [ \"$pkg\" != \"com.activision.callofduty.shooter\" ] && [ \"$pkg\" != \"com.epicgames.fortnite\" ] && [ \"$pkg\" != \"com.miHoYo.GenshinImpact\" ] && [ \"$pkg\" != \"com.HoYoverse.hkrpgoversea\" ] && [ \"$pkg\" != \"com.miHoYo.bh3global\" ] && [ \"$pkg\" != \"com.ea.gp.apexlegendsmobilefps\" ] && [ \"$pkg\" != \"com.kaixuan.blackholearena\" ] && [ \"$pkg\" != \"com.activision.warzonemobile\" ] && [ \"$pkg\" != \"com.blizzard.diablo.immortal\" ] && [ \"$pkg\" != \"com.netease.lifeafter\" ] && [ \"$pkg\" != \"com.pearlabyss.blackdesertm\" ] && [ \"$pkg\" != \"com.levelinfinite.hotta.gp\" ] && [ \"$pkg\" != \"com.kurogame.wutheringwaves\" ] && [ \"$pkg\" != \"com.nvsgames.DMC\" ]; then\n");
                os.writeBytes("    am force-stop \"$pkg\"\n");
                os.writeBytes("    killall -9 \"$pkg\" 2>/dev/null\n");
                os.writeBytes("  fi\n");
                os.writeBytes("done\n");

                // Boost BGMI
                os.writeBytes("am set-foreground-service com.pubg.imobile 1\n");
                os.writeBytes("dumpsys deviceidle whitelist +com.pubg.imobile\n");

                os.writeBytes("exit\n");
                os.flush();
                os.close();

                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(SafeKillerTileService.this, "Trubo Activated", Toast.LENGTH_SHORT).show());

            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(SafeKillerTileService.this, "Root required!", Toast.LENGTH_SHORT).show());
            }
        }).start());
    }
}