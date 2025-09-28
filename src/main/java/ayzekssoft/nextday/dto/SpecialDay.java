package ayzekssoft.nextday.dto;

import java.time.LocalDate;

public class SpecialDay {
    private LocalDate date;
    private String name;

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
