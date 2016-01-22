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
  float distance;

  public String fullAddress() {
    return address + "\n" + city + ", " + state + " " + zipCode + "\n" + officeNumber;
  }

  public String flatAddress() {
    return address + city + ", " + state + " " + zipCode;
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
    return "N/A".equals(officeNumber) ? "" : officeNumber;
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

  public float distance() {
    return this.distance;
  }

  public FieldOffice distance(final float distance) {
    this.distance = distance;
    return this;
  }

  @Override public String toString() {
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
}
