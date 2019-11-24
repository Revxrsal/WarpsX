/*
 * * Copyright 2019 github.com/ReflxctionDev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.reflxction.warps.warp;

import com.google.gson.InstanceCreator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.github.reflxction.warps.config.PluginSettings;
import io.github.reflxction.warps.util.compatibility.Compatibility;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a player warp
 */
public class PlayerWarp {

    @Expose
    private String key;

    @Expose
    private OfflinePlayer owner;

    @Expose
    private int delay = PluginSettings.DEFAULT_DELAY.get();

    @Expose
    private int exclusion = -1;

    @Expose
    private boolean isPublic;

    @Expose
    private Location location;

    @Expose
    private String greetingMessage = PluginSettings.DEFAULT_GREETING_MESSAGE.get();

    @Expose
    private List<OfflinePlayer> invited = new ArrayList<>();

    @Expose
    private List<OfflinePlayer> bannedPlayers = new ArrayList<>();

    @Expose
    @SerializedName("effects")
    private Set<PotionEffect> potionEffects = new HashSet<>();

    @Expose
    private Sound cue = Compatibility.getSound("ENTITY_ARROW_HIT_PLAYER", "ARROW_HIT");

    @Expose
    private boolean banned;

    public String getKey() {
        return key;
    }

    public int getDelay() {
        return delay;
    }

    public int getExclusion() {
        return exclusion;
    }

    public OfflinePlayer getOwner() {
        return owner;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public String getGreetingMessage() {
        return greetingMessage;
    }

    public Location getLocation() {
        return location;
    }

    public Set<PotionEffect> getPotionEffects() {
        return potionEffects;
    }

    public Sound getSound() {
        return cue;
    }

    public boolean isBanned(OfflinePlayer player) {
        return bannedPlayers.contains(player);
    }

    public List<OfflinePlayer> getBannedPlayers() {
        return bannedPlayers;
    }

    public void banPlayer(OfflinePlayer player) {
        bannedPlayers.add(player);
    }

    public void unbanPlayer(OfflinePlayer player) {
        bannedPlayers.remove(player);
    }

    public boolean isWarpBanned() {
        return banned;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setGreetingMessage(String greetingMessage) {
        this.greetingMessage = greetingMessage;
    }

    public void setExclusion(int exclusion) {
        this.exclusion = exclusion;
    }

    public void invite(OfflinePlayer player) {
        invited.add(player);
    }

    public void uninvite(OfflinePlayer player) {
        invited.remove(player);
    }

    public boolean isInvited(OfflinePlayer player) {
        if (isBanned(player)) return false;
        if (isPublic) return true;
        if (player instanceof Player && canModify((Player) player)) return true;
        return invited.contains(player);
    }

    public void setSound(Sound sound) {
        this.cue = sound;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public boolean canModify(CommandSender sender) {
        if (sender instanceof BlockCommandSender) return false;
        if (sender.hasPermission("warpsx.admin.modify")) return true;
        return owner.getUniqueId().equals(((Player) sender).getUniqueId());
    }

    public List<OfflinePlayer> getInvited() {
        return invited;
    }

    public static class Creator implements InstanceCreator<PlayerWarp> {

        /**
         * Gson invokes this call-back method during deserialization to create an instance of the
         * specified type. The fields of the returned instance are overwritten with the data present
         * in the Json. Since the prior contents of the object are destroyed and overwritten, do not
         * return an instance that is useful elsewhere. In particular, do not return a common instance,
         * always use {@code new} to create a new instance.
         *
         * @param type the parameterized T represented as a {@link Type}.
         * @return a default object instance of type T.
         */
        @Override
        public PlayerWarp createInstance(Type type) {
            return new PlayerWarp();
        }

    }

    public static class Builder {

        private PlayerWarp warp;

        public Builder() {
            this(new PlayerWarp());
        }

        public Builder(PlayerWarp warp) {
            this.warp = warp;
        }

        public Builder key(String key) {
            warp.key = key;
            return this;
        }

        public Builder owner(OfflinePlayer player) {
            warp.owner = player;
            return this;
        }

        public Builder delay(int delay) {
            warp.delay = delay;
            return this;
        }

        public Builder sound(Sound sound) {
            warp.cue = sound;
            return this;
        }

        public Builder greeting(String greeting) {
            warp.greetingMessage = greeting;
            return this;
        }

        public Builder location(Location location) {
            warp.location = location;
            return this;
        }

        public PlayerWarp build() {
            return warp;
        }


    }
}