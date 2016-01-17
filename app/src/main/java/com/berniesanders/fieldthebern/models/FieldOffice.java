package com.berniesanders.fieldthebern.models;

public class FieldOffice {

    private String state;
    private String address;
    private String city;
    private String zipCode;
    private String officeNumber;
    private String notes;
    private String officeLead;
    private Double lat;
    private Double lng;

    public String fullAddress() {
        return address
                + "\n"
                + city
                + ", "
                + state
                + " "
                + zipCode
                +"\n"
                + officeNumber;
    }
    public String flatAddress() {
        return address
                + city
                + ", "
                + state
                + " "
                + zipCode;
    }

    public String state() {
        return this.state;
    }

    public String address() {
        return this.address;
    }

    public String city() {
        return this.city;
    }

    public String zipCode() {
        return this.zipCode;
    }


    public String notes() {
        return this.notes;
    }

    public String officeLead() {
        return this.officeLead;
    }

    public String phone() {
        return this.officeNumber;
    }

    public Double lat() {
        return this.lat;
    }

    public Double lng() {
        return this.lng;
    }

    public FieldOffice state(final String state) {
        this.state = state;
        return this;
    }

    public FieldOffice address(final String address) {
        this.address = address;
        return this;
    }

    public FieldOffice city(final String city) {
        this.city = city;
        return this;
    }

    public FieldOffice zipCode(final String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public FieldOffice notes(final String notes) {
        this.notes = notes;
        return this;
    }

    public FieldOffice officeLead(final String officeLead) {
        this.officeLead = officeLead;
        return this;
    }

    public FieldOffice phone(final String officeNumber) {
        this.officeNumber = officeNumber;
        return this;
    }

    public FieldOffice lat(final Double lat) {
        this.lat = lat;
        return this;
    }

    public FieldOffice lng(final Double lng) {
        this.lng = lng;
        return this;
    }

    @Override
    public String toString() {
        return "FieldOffice{" +
                "state='" + state + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", officeNumber='" + officeNumber + '\'' +
                ", notes='" + notes + '\'' +
                ", officeLead='" + officeLead + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldOffice that = (FieldOffice) o;

        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (zipCode != null ? !zipCode.equals(that.zipCode) : that.zipCode != null) return false;
        if (officeNumber != null ? !officeNumber.equals(that.officeNumber) : that.officeNumber != null) {
            return false;
        }
        if (notes != null ? !notes.equals(that.notes) : that.notes != null) return false;
        if (officeLead != null ? !officeLead.equals(that.officeLead) : that.officeLead != null) {
            return false;
        }
        if (lat != null ? !lat.equals(that.lat) : that.lat != null) return false;
        return !(lng != null ? !lng.equals(that.lng) : that.lng != null);

    }

    @Override
    public int hashCode() {
        int result = state != null ? state.hashCode() : 0;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (zipCode != null ? zipCode.hashCode() : 0);
        result = 31 * result + (officeNumber != null ? officeNumber.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        result = 31 * result + (officeLead != null ? officeLead.hashCode() : 0);
        result = 31 * result + (lat != null ? lat.hashCode() : 0);
        result = 31 * result + (lng != null ? lng.hashCode() : 0);
        return result;
    }
}
