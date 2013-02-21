package com.thevoxelbox.voxelguest.modules.helper;

import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.thevoxelbox.voxelguest.persistence.Persistence;

/**
 *
 * @author TheCryoknight
 */
public class HelperManager
{
    private final Set<ReviewRequest> activeReviews = new HashSet<>();
    private final Map<String, Helper> helpers = new HashMap<>();
    private final Map<Player, GuestHistoryEntry> lastReview = new HashMap<>();

    /**
     * Opens up a new review request for the player provided
     *
     * @param player Player to create review for.
     */
    public void newReview(Player player)
    {
        ReviewRequest review = new ReviewRequest(player, player.getLocation());
        this.activeReviews.add(review);
        this.notifyForNewReview(review);
    }

    public ReviewRequest getReview(final Player guest)
    {
        for (ReviewRequest review : this.activeReviews)
        {
            if (review.getGuest().equals(guest))
            {
                return review;
            }
        }
        return null;
    }

    /**
     * Closes a review request and stores a record of it to the database.
     *
     * @param helper The helper closing the review
     * @param review The review to be closed
     */
    public void closeReview(Player helper, ReviewRequest review)
    {
        this.activeReviews.remove(review);
        GuestHistoryEntry reviewHistorical = new GuestHistoryEntry(review.getGuest().getName(), helper.getName());
        Persistence.getInstance().save(reviewHistorical);
        this.lastReview.put(helper, reviewHistorical);
        Helper helperObj = this.getHelper(helper);
        if (helperObj != null)
        {
            helperObj.review();
        }
        Persistence.getInstance().save(helperObj);
    }

    /**
     * Adds a comment to the last review performed by the specified helper
     *
     * @param helper Helper adding a comment
     * @param comment The comment being added
     */
    public void addComment(Player helper, String comment)
    {
        final GuestHistoryEntry historyEntry = this.lastReview.get(helper);
        if (historyEntry != null)
        {
            historyEntry.setComment(comment);
            Persistence.getInstance().save(historyEntry);
            helper.sendMessage(ChatColor.GRAY + "Comment successfuly added");
        }
        else
        {
            helper.sendMessage(ChatColor.RED + "You havent reviewed anyone yet!");
        }
    }

    /**
     * Adds a helper to the non-administrator list of helpers and saves them to the database (administrator list is permission based).
     *
     * @param newHelper Player to add as a helper
     */
    public void addHelper(Player newHelper)
    {
        final Helper newHelperObj = new Helper(newHelper.getName());
        Persistence.getInstance().save(newHelperObj);
        this.helpers.put(newHelperObj.getName(), newHelperObj);
    }

    /**
     * Removes a helper from the non-administrator list of helpers and deletes them to the database (administrator list is permission based).
     * 
     * @param oldHelper
     */
    public boolean removeHelper(Helper oldHelper)
    {
        if (this.helpers.containsKey(oldHelper.getName()))
        {
            return false;
        }
        Persistence.getInstance().delete(oldHelper);
        this.helpers.remove(oldHelper.getName());
        return true;
    }

    /**
     * Initializes the list of non-administrator helpers to ram
     */
    public void initHelperList()
    {
        List<Helper> tmpHelperList = Persistence.getInstance().loadAll(Helper.class);
        for (Helper helper : tmpHelperList)
        {
            this.helpers.put(helper.getName(), helper);
        }
    }

    /**
     * Gets the helper object corresponding with the player provided or returns null if no player on list.
     *
     * @param helper Player to find corresponding helper object
     * @return helper object corresponding with the player
     */
    public Helper getHelper(Player helper)
    {
        return this.helpers.get(helper.getName());
    }

    /**
     * Creates a highly formated string containing all information related to the
     * current list of active reviews OR returns null if there are no currently active reviews.
     * <br />
     * <strong>Note:</strong> Will not show reviews for people who are currently not online.
     *
     * @return A formatted string of all active reviews
     */
    public String getActiveRequests()
    {
        if (this.activeReviews.isEmpty())
        {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ChatColor.DARK_GRAY + "========================\n");
        stringBuilder.append(ChatColor.DARK_AQUA + "Whitelist Requests\n");
        for (ReviewRequest request : this.activeReviews)
        {
            if (request.getGuest().isOnline())
            {
                stringBuilder.append(ChatColor.GRAY + "Name" + ChatColor.WHITE + ":" + ChatColor.GOLD + request.getGuest().getName() + "(" + ChatColor.DARK_AQUA + this.getGuestHistory(request.getGuest().getName()).size() + ")\n");
            }
        }
        stringBuilder.append(ChatColor.DARK_GRAY + "========================\n");
        return stringBuilder.toString();
    }

    /**
     * Notifies all of the currently online helpers that there is a new whitelist review
     *
     * @param review
     */
    public void notifyForNewReview(ReviewRequest review)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ChatColor.DARK_GRAY + "========================\n");
        stringBuilder.append(ChatColor.DARK_AQUA + "New Whitelist Review\n");
        stringBuilder.append(ChatColor.GRAY + "Name" + ChatColor.WHITE + ": " + ChatColor.GOLD + review.getGuest().getName() + "\n");
        stringBuilder.append(ChatColor.DARK_GRAY + "========================\n");

        this.notifyHelpers(stringBuilder.toString());
    }

    /**
     * Core notify method sends specified string to all online helpers.
     *
     * @param messageForHelpers
     */
    public void notifyHelpers(String messageForHelpers)
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            boolean isHelper = false;
            if (this.helpers.containsKey(player.getName()))
            {
                isHelper = true;
            }
            else if (player.hasPermission("voxelguest.helper.adminhelper"))
            {
                isHelper = true;
            }
            if (isHelper)
            {
                player.sendMessage(messageForHelpers);
            }
        }
    }

    /**
     * Searches through the database an finds all historical guest reviews for the name provided.
     *
     * @param playerName Name of the guest to search for history
     * @return List of all review history
     */
    public List<GuestHistoryEntry> getGuestHistory(String playerName)
    {
        HashMap<String, Object> selectRestrictions = new HashMap<>();
        selectRestrictions.put("guestName", playerName);
        return Persistence.getInstance().loadAll(GuestHistoryEntry.class, selectRestrictions);
    }

    public void sendHelperGuestHistory(Player helper, String guestName)
    {
        final List<GuestHistoryEntry> history = this.getGuestHistory(guestName); 
        if (history.isEmpty())
        {
            return;
        }
        Collections.sort(history);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ChatColor.DARK_GRAY + "=====================================\n");
        stringBuilder.append(ChatColor.DARK_AQUA + "Whitelist Review History for" + ChatColor.GRAY + ": " + guestName + "\n");
        stringBuilder.append(ChatColor.DARK_GRAY + "=====================================\n");

        final ListIterator<GuestHistoryEntry> reviewListItr = history.listIterator();
        while (reviewListItr.hasNext())
        {
            //TODO: Time zone stuffs
            final GuestHistoryEntry entry = reviewListItr.next();
            final Calendar date = new GregorianCalendar();
            date.setTimeInMillis(entry.getReviewTime());
            String dateStr = date.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US) + ". " + date.get(Calendar.DAY_OF_MONTH) + ", " + date.get(Calendar.YEAR) + " at " + date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE);
            stringBuilder.append(ChatColor.DARK_AQUA + "(" + ChatColor.GOLD + reviewListItr.previousIndex() + ChatColor.DARK_AQUA + ")" + ChatColor.GRAY + " by " + ChatColor.GOLD + entry.getReviewerName() + ChatColor.GRAY + " on " + ChatColor.DARK_AQUA + dateStr + ChatColor.GRAY + ChatColor.ITALIC + "- " + entry.getComment() + "\n");
        }
        stringBuilder.append(ChatColor.DARK_GRAY + "=====================================\n");

        helper.sendMessage(stringBuilder.toString());
    }
}
