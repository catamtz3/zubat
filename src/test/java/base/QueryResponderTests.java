package base;

import cse332.interfaces.QueryResponder;
import cse332.types.CensusGroup;
import main.PopulationQuery;

// Base utility class that initializes data before tests.
public class QueryResponderTests {
    protected static QueryResponder STUDENT_100_500;
    protected static QueryResponder STUDENT_20_40;

    protected static CensusGroup[] readCensusdata() {
        return PopulationQuery.parse("CenPop2010.txt");
    }
}
