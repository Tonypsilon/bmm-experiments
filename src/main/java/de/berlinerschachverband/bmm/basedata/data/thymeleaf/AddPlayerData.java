package de.berlinerschachverband.bmm.basedata.data.thymeleaf;

import java.util.Optional;

public class AddPlayerData {

    private Integer zps;
    private Integer memberNumber;
    private String fullName;
    private String surname;
    private Integer dwz;
    private Integer currentBoardNumber;

    public Integer getZps() {
        return zps;
    }

    public void setZps(Integer zps) {
        this.zps = zps;
    }

    public Integer getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(Integer memberNumber) {
        this.memberNumber = memberNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Optional<Integer> getDwz() {
        return Optional.ofNullable(dwz);
    }

    public void setDwz(Integer dwz) {
        this.dwz = dwz;
    }

    public Integer getCurrentBoardNumber() {
        return currentBoardNumber;
    }

    public void setCurrentBoardNumber(Integer currentBoardNumber) {
        this.currentBoardNumber = currentBoardNumber;
    }

    public Boolean isSelectedOption(Integer boardNumber) {
        return boardNumber.equals(currentBoardNumber);
    }
}
