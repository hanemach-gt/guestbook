package com.company.serverexample.dao;

import com.company.serverexample.model.GuestBookEntry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GuestBookDao {

    private Connection connection;

    public GuestBookDao(Connection connection) {

        this.connection = connection;
    }

    public boolean insert(GuestBookEntry gbe) {

        String sql = "INSERT INTO guestbookentries(author_name, message, date_added) VALUES (?, ?, ?);";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, gbe.getAuthorName());
            stmt.setString(2, gbe.getMessage());
            stmt.setLong(3, gbe.getDateAdded().getTime());

            final int rowsUpdated = stmt.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {

            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return false;
    }

    public List<GuestBookEntry> getAllEntries() {

        List<GuestBookEntry> entries = new ArrayList<>();

        String sql = "SELECT author_name, message, date_added FROM guestbookentries;";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            /* The ResultSet is contained inside try-with-resources construct to guarantee its closure. */
            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    entries.add(resultSetToGuestBookEntry(rs));
                }
            }
        } catch (SQLException e) {

            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return entries;
    }

    private GuestBookEntry resultSetToGuestBookEntry(ResultSet rs) throws SQLException {

        String authorName = rs.getString("author_name");
        String message = rs.getString("message");
        Date dateAdded = new Date(rs.getLong("date_added"));

        return new GuestBookEntry(authorName, message, dateAdded);
    }
}
