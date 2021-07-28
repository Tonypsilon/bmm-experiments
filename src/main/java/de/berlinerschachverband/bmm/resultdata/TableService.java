package de.berlinerschachverband.bmm.resultdata;

import de.berlinerschachverband.bmm.basedata.data.DivisionData;
import de.berlinerschachverband.bmm.resultdata.data.thymeleaf.RegularDivisionTableData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TableService {

    public List<RegularDivisionTableData> getRegularTable(DivisionData divisionData) {
        List<RegularDivisionTableData> regularTableData = new ArrayList<>();

        return regularTableData;
    }

}
