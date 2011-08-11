/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laytonsmith.aliasengine.functions;

import com.laytonsmith.aliasengine.functions.exceptions.CancelCommandException;
import com.laytonsmith.aliasengine.functions.exceptions.ConfigRuntimeException;
import com.laytonsmith.aliasengine.Constructs.CArray;
import com.laytonsmith.aliasengine.Constructs.CString;
import com.laytonsmith.aliasengine.Constructs.CVoid;
import com.laytonsmith.aliasengine.Constructs.Construct;
import com.laytonsmith.aliasengine.Static;
import com.laytonsmith.aliasengine.functions.Exceptions.ExceptionType;
import java.io.File;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.Achievement;
import org.bukkit.Effect;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Server;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

/**
 * I'm So Meta, Even This Acronym
 * @author Layton
 */
public class Meta {

    public static String docs() {
        return "These functions provide a way to run other commands";
    }

    @api
    public static class runas implements Function {

        public String getName() {
            return "runas";
        }

        public Integer[] numArgs() {
            return new Integer[]{2};
        }

        public Construct exec(int line_num, File f, Player p, Construct... args) throws CancelCommandException, ConfigRuntimeException {
            if (args[1].val() == null || args[1].val().length() <= 0 || args[1].val().charAt(0) != '/') {
                throw new ConfigRuntimeException("The first character of the command must be a forward slash (i.e. '/give')",
                        ExceptionType.FormatException, line_num, f);
            }
            String cmd = args[1].val().substring(1);
            if (args[0] instanceof CArray) {
                CArray u = (CArray) args[0];
                for (int i = 0; i < u.size(); i++) {
                    exec(line_num, f, p, new Construct[]{new CString(u.get(i, line_num).val(), line_num, f), args[1]});
                }
                return new CVoid(line_num, f);
            }
            if (args[0].val().equals("~op")) {
                Player m = new AlwaysOpPlayer(p);
                if ((Boolean) Static.getPreferences().getPreference("debug-mode")) {
                    Static.getLogger().log(Level.INFO, "[CommandHelper]: Executing command on " + m.getName() + " (running as op): /" + cmd);
                }
                Static.getServer().dispatchCommand(m, cmd);
            } else {
                Player m = Static.getServer().getPlayer(args[0].val());
                if (m != null && m.isOnline()) {
                    if ((Boolean) Static.getPreferences().getPreference("debug-mode")) {
                        Static.getLogger().log(Level.INFO, "[CommandHelper]: Executing command on " + p.getName() + " (as " + m.getName() + "): /" + cmd.trim());
                    }
                    Static.getServer().dispatchCommand(m, cmd);
                } else {
                    throw new ConfigRuntimeException("The player " + args[0].val() + " is not online",
                            ExceptionType.PlayerOfflineException, line_num, f);
                }
            }
            return new CVoid(line_num, f);
        }
        
        public ExceptionType[] thrown(){
            return new ExceptionType[]{ExceptionType.FormatException, ExceptionType.PlayerOfflineException};
        }

        public String docs() {
            return "void {player, command} Runs a command as a particular user. The special user '~op' is a user that runs as op. Be careful with this very powerful function."
                    + " Commands cannot be run as an offline player. Returns void. If the first argument is an array of usernames, the command"
                    + " will be run in the context of each user in the array.";
        }

        public boolean isRestricted() {
            return true;
        }

        public void varList(IVariableList varList) {
        }

        public boolean preResolveVariables() {
            return true;
        }

        public String since() {
            return "3.0.1";
        }

        public Boolean runAsync() {
            return false;
        }
    }

    @api
    public static class run implements Function {

        public String getName() {
            return "run";
        }

        public Integer[] numArgs() {
            return new Integer[]{1};
        }

        public Construct exec(int line_num, File f, Player p, Construct... args) throws CancelCommandException, ConfigRuntimeException {
            if (args[0].val() == null || args[0].val().length() <= 0 || args[0].val().charAt(0) != '/') {
                throw new ConfigRuntimeException("The first character of the command must be a forward slash (i.e. '/give')",
                        ExceptionType.FormatException, line_num, f);
            }
            String cmd = args[0].val().substring(1);
            if ((Boolean) Static.getPreferences().getPreference("debug-mode")) {
                Static.getLogger().log(Level.INFO, "[CommandHelper]: Executing command on " + p.getName() + ": " + args[0].val().trim());
            }
            Static.getServer().dispatchCommand(p, cmd);
            return new CVoid(line_num, f);
        }

        public String docs() {
            return "void {var1} Runs a command as the current player. Useful for running commands in a loop. Note that this accepts commands like from the "
                    + "chat; with a forward slash in front.";
        }
        
        public ExceptionType[] thrown(){
            return new ExceptionType[]{ExceptionType.FormatException};
        }

        public boolean isRestricted() {
            return false;
        }

        public void varList(IVariableList varList) {
        }

        public boolean preResolveVariables() {
            return true;
        }

        public String since() {
            return "3.0.1";
        }

        public Boolean runAsync() {
            return false;
        }
    }

    @api
    public static class g implements Function {

        public String getName() {
            return "g";
        }

        public Integer[] numArgs() {
            return new Integer[]{Integer.MAX_VALUE};
        }

        public Construct exec(int line_num, File f, Player p, Construct... args) throws CancelCommandException, ConfigRuntimeException {
            for (int i = 0; i < args.length; i++) {
                args[i].val();
            }
            return new CVoid(line_num, f);
        }

        public String docs() {
            return "string {func1, [func2...]} Groups any number of functions together, and returns void. ";
        }
        
        public ExceptionType[] thrown(){
            return new ExceptionType[]{};
        }

        public boolean isRestricted() {
            return false;
        }

        public void varList(IVariableList varList) {
        }

        public boolean preResolveVariables() {
            return true;
        }

        public String since() {
            return "3.0.1";
        }

        public Boolean runAsync() {
            return null;
        }
    }
    
    @api
    public static class p implements Function{

        public String getName() {
            return "p";
        }

        public Integer[] numArgs() {
            return new Integer[]{1};
        }

        public String docs() {
            return "mixed {c} Used internally by the compiler.";
        }

        public ExceptionType[] thrown() {
            return null;
        }

        public boolean isRestricted() {
            return false;
        }

        public void varList(IVariableList varList) {}

        public boolean preResolveVariables() {
            return true;
        }

        public String since() {
            return "3.1.2";
        }

        public Boolean runAsync() {
            return null;
        }

        public Construct exec(int line_num, File f, Player p, Construct... args) throws ConfigRuntimeException {
            return Static.resolveConstruct(args[0].val(), line_num, f);
        }
        
    }

    @api
    public static class eval implements Function {

        public String getName() {
            return "eval";
        }

        public Integer[] numArgs() {
            return new Integer[]{1};
        }

        public String docs() {
            return "string {script_string} Executes arbitrary MScript. Note that this function is very experimental, and is subject to changing or "
                    + "removal.";
        }
        
        public ExceptionType[] thrown(){
            return new ExceptionType[]{};
        }

        public boolean isRestricted() {
            return true;
        }

        public void varList(IVariableList varList) {
        }

        public boolean preResolveVariables() {
            return true;
        }

        public String since() {
            return "3.1.0";
        }

        public Construct exec(int line_num, File f, Player p, Construct... args) throws CancelCommandException, ConfigRuntimeException {
            return new CVoid(line_num, f);
        }
        //Doesn't matter, run out of state anyways

        public Boolean runAsync() {
            return null;
        }
    }

    private static class AlwaysOpPlayer implements Player {

        public Player r;

        public AlwaysOpPlayer(Player realPlayer) {
            r = realPlayer;
        }

        public boolean isOnline() {
            return r.isOnline();
        }

        public String getDisplayName() {
            return r.getDisplayName();
        }

        public void setDisplayName(String string) {
            r.setDisplayName(string);
        }

        public void setCompassTarget(Location lctn) {
            r.setCompassTarget(lctn);
        }

        public Location getCompassTarget() {
            return r.getCompassTarget();
        }

        public InetSocketAddress getAddress() {
            return r.getAddress();
        }

        public void sendRawMessage(String string) {
            r.sendRawMessage(string);
        }

        public void kickPlayer(String string) {
            r.kickPlayer(string);
        }

        public void chat(String string) {
            r.chat(string);
        }

        public boolean performCommand(String string) {
            return r.performCommand(string);
        }

        public boolean isSneaking() {
            return r.isSneaking();
        }

        public void setSneaking(boolean bln) {
            r.setSneaking(bln);
        }

        public void saveData() {
            r.saveData();
        }

        public void loadData() {
            r.loadData();
        }

        @Deprecated
        public void updateInventory() {
            r.updateInventory();
        }

        public String getName() {
            return r.getName();
        }

        public PlayerInventory getInventory() {
            return r.getInventory();
        }

        public ItemStack getItemInHand() {
            return r.getItemInHand();
        }

        public void setItemInHand(ItemStack is) {
            r.setItemInHand(is);
        }

        public boolean isSleeping() {
            return r.isSleeping();
        }

        public int getSleepTicks() {
            return r.getSleepTicks();
        }

        public int getHealth() {
            return r.getHealth();
        }

        public void setHealth(int i) {
            r.setHealth(i);
        }

        public double getEyeHeight() {
            return r.getEyeHeight();
        }

        public double getEyeHeight(boolean bln) {
            return r.getEyeHeight(bln);
        }

        public Location getEyeLocation() {
            return r.getEyeLocation();
        }

        public List<Block> getLineOfSight(HashSet<Byte> hs, int i) {
            return r.getLineOfSight(hs, i);
        }

        public Block getTargetBlock(HashSet<Byte> hs, int i) {
            return r.getTargetBlock(hs, i);
        }

        public List<Block> getLastTwoTargetBlocks(HashSet<Byte> hs, int i) {
            return r.getLastTwoTargetBlocks(hs, i);
        }

        public Egg throwEgg() {
            return r.throwEgg();
        }

        public Snowball throwSnowball() {
            return r.throwSnowball();
        }

        public Arrow shootArrow() {
            return r.shootArrow();
        }

        public boolean isInsideVehicle() {
            return r.isInsideVehicle();
        }

        public boolean leaveVehicle() {
            return r.leaveVehicle();
        }

        public Vehicle getVehicle() {
            return r.getVehicle();
        }

        public int getRemainingAir() {
            return r.getRemainingAir();
        }

        public void setRemainingAir(int i) {
            r.setRemainingAir(i);
        }

        public int getMaximumAir() {
            return r.getMaximumAir();
        }

        public void setMaximumAir(int i) {
            r.setMaximumAir(i);
        }

        public void damage(int i) {
            r.damage(i);
        }

        public void damage(int i, Entity entity) {
            r.damage(i, entity);
        }

        public int getMaximumNoDamageTicks() {
            return r.getMaximumNoDamageTicks();
        }

        public void setMaximumNoDamageTicks(int i) {
            r.setMaximumNoDamageTicks(i);
        }

        public int getLastDamage() {
            return r.getLastDamage();
        }

        public void setLastDamage(int i) {
            r.setLastDamage(i);
        }

        public int getNoDamageTicks() {
            return r.getNoDamageTicks();
        }

        public void setNoDamageTicks(int i) {
            r.setNoDamageTicks(i);
        }

        public Location getLocation() {
            return r.getLocation();
        }

        public void setVelocity(Vector vector) {
            r.setVelocity(vector);
        }

        public Vector getVelocity() {
            return r.getVelocity();
        }

        public World getWorld() {
            return r.getWorld();
        }

        public boolean teleport(Location lctn) {
            return r.teleport(lctn);
        }

        public boolean teleport(Entity entity) {
            return r.teleport(entity);
        }

//        @Deprecated
//        public void teleportTo(Location lctn) {
//            r.teleportTo(lctn);
//        }
//
//        @Deprecated
//        public void teleportTo(Entity entity) {
//            r.teleportTo(entity);
//        }

        public List<Entity> getNearbyEntities(double d, double d1, double d2) {
            return r.getNearbyEntities(d, d1, d2);
        }

        public int getEntityId() {
            return r.getEntityId();
        }

        public int getFireTicks() {
            return r.getFireTicks();
        }

        public int getMaxFireTicks() {
            return r.getMaxFireTicks();
        }

        public void setFireTicks(int i) {
            r.setFireTicks(i);
        }

        public void remove() {
            r.remove();
        }

        public boolean isDead() {
            return r.isDead();
        }

        public Server getServer() {
            return r.getServer();
        }

        public Entity getPassenger() {
            return r.getPassenger();
        }

        public boolean setPassenger(Entity entity) {
            return r.setPassenger(entity);
        }

        public boolean isEmpty() {
            return r.isEmpty();
        }

        public boolean eject() {
            return r.eject();
        }

        public float getFallDistance() {
            return r.getFallDistance();
        }

        public void setFallDistance(float f) {
            r.setFallDistance(f);
        }

        public void sendMessage(String string) {
            r.sendMessage(string);
        }

        public boolean isOp() {
            return true;
        }

        public void setSleepingIgnored(boolean bln) {
            r.setSleepingIgnored(bln);
        }

        public boolean isSleepingIgnored() {
            return r.isSleepingIgnored();
        }

        public void awardAchievement(Achievement a) {
            r.awardAchievement(a);
        }

        public void incrementStatistic(Statistic ststc) {
            r.incrementStatistic(ststc);
        }

        public void incrementStatistic(Statistic ststc, int i) {
            r.incrementStatistic(ststc, Material.AIR);
        }

        public void incrementStatistic(Statistic ststc, Material mtrl) {
            r.incrementStatistic(ststc, mtrl);
        }

        public void incrementStatistic(Statistic ststc, Material mtrl, int i) {
            r.incrementStatistic(ststc, mtrl, i);
        }

        public void playNote(Location loc, byte instrument, byte note) {
            r.playNote(loc, instrument, note);
        }

        public void sendBlockChange(Location loc, Material material, byte data) {
            r.sendBlockChange(loc, material, data);
        }

        public void sendBlockChange(Location loc, int material, byte data) {
            r.sendBlockChange(loc, material, data);
        }

        public void setLastDamageCause(EntityDamageEvent event) {
            r.setLastDamageCause(event);
        }

        public EntityDamageEvent getLastDamageCause() {
            return r.getLastDamageCause();
        }

        public void playNote(Location loc, Instrument instrument, Note note) {
            r.playNote(loc, instrument, note);
        }

        public void playEffect(Location loc, Effect effect, int data) {
            r.playEffect(loc, effect, data);
        }

        public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {
            return r.sendChunkChange(loc, sx, sy, sz, data);
        }

        public void setPlayerTime(long time, boolean relative) {
            r.setPlayerTime(time, relative);
        }

        public long getPlayerTime() {
            return r.getPlayerTime();
        }

        public long getPlayerTimeOffset() {
            return r.getPlayerTimeOffset();
        }

        public boolean isPlayerTimeRelative() {
            return r.isPlayerTimeRelative();
        }

        public void resetPlayerTime() {
            r.resetPlayerTime();
        }

        public UUID getUniqueId() {
            return r.getUniqueId();
        }

        public boolean isPermissionSet(String name) {
            return r.isPermissionSet(name);
        }

        public boolean isPermissionSet(Permission perm) {
            return r.isPermissionSet(perm);
        }

        public boolean hasPermission(String name) {
            return r.hasPermission(name);
        }

        public boolean hasPermission(Permission perm) {
            return r.hasPermission(perm);
        }

        public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
            return r.addAttachment(plugin, name, value);
        }

        public PermissionAttachment addAttachment(Plugin plugin) {
            return r.addAttachment(plugin);
        }

        public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
            return r.addAttachment(plugin, name, value, ticks);
        }

        public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
            return r.addAttachment(plugin, ticks);
        }

        public void removeAttachment(PermissionAttachment attachment) {
            r.removeAttachment(attachment);
        }

        public void recalculatePermissions() {
            r.recalculatePermissions();
        }

        public Set<PermissionAttachmentInfo> getEffectivePermissions() {
            return r.getEffectivePermissions();
        }

        public void setOp(boolean value) {
            r.setOp(value);
        }
    }
}
