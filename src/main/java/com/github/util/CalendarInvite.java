package com.github.util;

import java.util.Optional;
import java.util.UUID;

public class CalendarInvite {

    private StringBuffer calendar = null;

    private CalendarInvite(Builder builder) {
        calendar = new StringBuffer("BEGIN:VCALENDAR\n")
                .append("METHOD:").append(Optional.ofNullable(builder.method).orElse("REQUEST")).append("\n")
                .append("PRODID:").append(Optional.ofNullable(builder.prodId).orElse("//Microsoft Corporation//Outlook 9.0 MIMEDIR//EN")).append("\n")
                .append("VERSION:").append(Optional.ofNullable(builder.version).orElse("2.0")).append("\n")
                .append("BEGIN:VEVENT\n")
                    .append("DTSTART:").append(builder.eventStartTime).append("\n")
                    .append("DTEND:").append(builder.eventEndTime).append("\n")
                    .append("UID:").append(Optional.ofNullable(builder.eventId).orElse(UUID.randomUUID().toString())).append("\n")
                    .append("LOCATION:").append(builder.eventLocation).append("\n")
                    .append("SEQUENCE:").append(Optional.ofNullable(builder.sequence).orElse("0")).append("\n")
                    .append("PRIORITY:").append(Optional.ofNullable(builder.eventCreatedTime).orElse("1")).append("\n")
                    .append("CLASS:").append(Optional.ofNullable(builder.inviteClass).orElse("PUBLIC")).append("\n")
                    .append("TRANSP:").append(Optional.ofNullable(builder.transp).orElse("OPAQUE")).append("\n")
                    .append("DESCRIPTION:").append(Optional.ofNullable(builder.calendarDescription).orElse("Meeting Invite")).append("\n")
                    .append("BEGIN:VALARM").append("\n")
                        .append("TRIGGER:;RELATED=START:-").append(Optional.ofNullable(builder.alarmTrigger).orElse("PT00H15M00S")).append("\n")
                        .append("ACTION").append(Optional.ofNullable(builder.alarmAction).orElse("Display")).append("\n")
                        .append("DESCRIPTION:").append(Optional.ofNullable(builder.alarmDescription).orElse("Reminder")).append("\n")
                    .append("END:VALARM\n")
                .append("END:VEVENT\n")
                .append("END:VCALENDAR");
    }


    @Override
    public String toString() {
        return calendar.toString();
    }

    public static class Builder {

        private String prodId;
        private String version;
        private String method;
        private String inviteClass;
        private final String eventStartTime;
        private final String eventEndTime;
        private final String eventLocation;
        private String transp;
        private String sequence;
        private String eventId;
        private String eventCreatedTime;
        private String calendarDescription;
        private String alarmTrigger;
        private String alarmAction;
        private String alarmDescription;

        public Builder(String eventStartTime, String eventEndTime, String eventLocation) {
            this.eventStartTime = eventStartTime;
            this.eventEndTime = eventEndTime;
            this.eventLocation = eventLocation;
        }


        public Builder withProdId(String prodId) {
            this.prodId = prodId;
            return this;
        }

        public Builder withVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder withMethod(String method) {
            this.method = method;
            return this;
        }

        public Builder withClass(String inviteClass) {
            this.inviteClass = inviteClass;
            return this;
        }

        public Builder withTransp(String transp) {
            this.transp = transp;
            return this;
        }

        public Builder withSequence(String sequence) {
            this.sequence = sequence;
            return this;
        }

        public Builder withEventId(String eventId) {
            this.eventId = eventId;
            return this;
        }

        public Builder withEventCreatedTime(String eventCreatedTime) {
            this.eventCreatedTime = eventCreatedTime;
            return this;
        }

        public Builder withAlarmAction(String alarmAction) {
            this.alarmAction = alarmAction;
            return this;
        }

        public Builder withAlarmTrigger(String alarmTrigger) {
            this.alarmTrigger = alarmTrigger;
            return this;
        }

        public Builder withAlarmDescription(String alarmDescription) {
            this.alarmDescription = alarmDescription;
            return this;
        }

        public Builder withCalendarDescription(String calendarDescription) {
            this.calendarDescription = calendarDescription;
            return this;
        }

        public CalendarInvite build() {
            return new CalendarInvite(this);
        }
    }
}
