package de.berlinerschachverband.bmm.basedata.service;

import de.berlinerschachverband.bmm.exceptions.AvailablePlayerNotFoundException;
import de.berlinerschachverband.bmm.basedata.data.AvailablePlayer;
import de.berlinerschachverband.bmm.basedata.data.AvailablePlayerData;
import de.berlinerschachverband.bmm.basedata.data.AvailablePlayerRepository;
import org.springframework.stereotype.Service;

@Service
public class AvailablePlayerService {

    private final AvailablePlayerRepository availablePlayerRepository;

    public AvailablePlayerService(AvailablePlayerRepository availablePlayerRepository) {
        this.availablePlayerRepository = availablePlayerRepository;
    }

    public AvailablePlayerData getAvailablePlayerByZpsAndMemberNumber(Integer zps, Integer memberNumber) {
        return toAvailablePlayerData(availablePlayerRepository.findByZpsAndMemberNumber(zps, memberNumber).orElseThrow(
                () -> {
                    throw new AvailablePlayerNotFoundException(zps, memberNumber);
                }));
    }

    public void deleteAvailablePlayer(Integer zps, Integer memberNumber) {
        availablePlayerRepository.delete(availablePlayerRepository.findByZpsAndMemberNumber(zps, memberNumber).orElseThrow(
                () -> {
                    throw new AvailablePlayerNotFoundException(zps, memberNumber);
                }));
    }

    public AvailablePlayerData toAvailablePlayerData(AvailablePlayer availablePlayer) {
        return new AvailablePlayerData(availablePlayer.getId(),
                availablePlayer.getZps(),
                availablePlayer.getMemberNumber(),
                availablePlayer.getActive(),
                availablePlayer.getFullName(),
                availablePlayer.getSurname(),
                availablePlayer.getBirthYear(),
                availablePlayer.getDwz(),
                availablePlayer.getElo(),
                availablePlayer.getTitle());
    }
}
