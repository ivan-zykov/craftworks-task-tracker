package net.ivanzykov.craftworkstasktracker;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents Task entity for external clients and converts time to client's or db's time zone.
 */
public class TaskDto {

    private Long id;
    private String createdAt;
    private String updatedAt;
    private String dueDate;
    private String resolvedAt;
    private String title;
    private String description;
    private String priority;
    private String status;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm zz");
    private ZoneId timezone;

    public TaskDto() {
        // Required by Jackson
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(String resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    /**
     * Parses field with date when the task was created and converts it to the supplied timezone.
     *
     * @param timezone  zoneId object of the client's timezone
     * @return          converted date and time when the task was created
     */
    public OffsetDateTime getCreatedAtConverted(ZoneId timezone) {
        return parseAndConvertDate(timezone, createdAt);
    }

    /**
     * Parses field with date when the task was updated and converts it to the supplied timezone.
     *
     * @param timezone  zoneId object of the client's timezone
     * @return          converted date and time when the task was updated
     */
    public OffsetDateTime getUpdatedAtConverted(ZoneId timezone) {
        return parseAndConvertDate(timezone, updatedAt);
    }

    /**
     * Parses field with date when the task is expected to be done and converts it to the supplied timezone.
     *
     * @param timezone  zoneId object of the client's timezone
     * @return          converted date and time when the task is expected to be done
     */
    public OffsetDateTime getDueDateConverted(ZoneId timezone) {
        return parseAndConvertDate(timezone, dueDate);
    }

    /**
     * Parses field with date when the task was done and converts it to the supplied timezone.
     *
     * @param timezone  zoneId object of the client's timezone
     * @return          converted date and time when the task was done
     */
    public OffsetDateTime getResolvedAtConverted(ZoneId timezone) {
        return parseAndConvertDate(timezone, resolvedAt);
    }

    private OffsetDateTime parseAndConvertDate(ZoneId timezone, String date) {
        if (date != null) {
            return ZonedDateTime.parse(date, dateTimeFormatter)
                    .withZoneSameInstant(timezone)
                    .toOffsetDateTime();
        }
        return null;
    }

    /**
     * Converts all time-related fields to the time zone of the client.
     *
     * @param task      task object of the corresponding task
     * @param timezone  zoneId object of the client's timezone
     */
    public void setAllDatesConverted(Task task, ZoneId timezone) {
        this.timezone = timezone;
        setCreatedAtConverted(task.getCreatedAt());
        setUpdatedAtConverted(task.getUpdatedAt());
        setDueDateConverted(task.getDueDate());
        setResolvedAtConverted(task.getResolvedAt());
    }

    /**
     * Converts the date when the task was created to the client's time zone, formats it to string and updates the
     * corresponding field of this object.
     *
     * @param createdAt offsetDateTime object the task was created
     */
    public void setCreatedAtConverted(OffsetDateTime createdAt) {
        this.createdAt = convertAndFormatDate(createdAt);
    }

    /**
     * Converts the date when the task was last updated to the client's time zone, formats it to string and updates the
     * corresponding field of this object.
     *
     * @param updatedAt offsetDateTime object the task was last updated
     */
    public void setUpdatedAtConverted(OffsetDateTime updatedAt) {
        this.updatedAt = convertAndFormatDate(updatedAt);
    }

    /**
     * Converts the date when the task is expected to be done to the client's time zone, formats it to string and
     * updates the corresponding field of this object
     *
     * @param dueDate   offsetDateTime object when the task is expected to be done
     */
    public void setDueDateConverted(OffsetDateTime dueDate) {
        this.dueDate = convertAndFormatDate(dueDate);
    }

    /**
     * Converts the date when the task was done to the client's time zone, formats it to string and updates the
     * corresponding field of this object.
     *
     * @param resolvedAt    offsetDateTime object when the task was done
     */
    public void setResolvedAtConverted(OffsetDateTime resolvedAt) {
        // resolvedAt allowed to be null
        if (resolvedAt != null) {
            this.resolvedAt = convertAndFormatDate(resolvedAt);
        }
    }

    private String convertAndFormatDate(OffsetDateTime date) {
        return date.atZoneSameInstant(timezone)
                .format(dateTimeFormatter);
    }
}
