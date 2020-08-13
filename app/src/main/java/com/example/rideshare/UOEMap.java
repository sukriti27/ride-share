package com.example.rideshare;

import java.util.ArrayList;
import java.util.List;

/**
 * A class with a single static list of all the University of Edinburgh buildings
 *
 * @author Sukriti
 * @version 1.0
 */
public class UOEMap {

    public static List<POI> pointsOfInterest;

    static {

        pointsOfInterest = new ArrayList<POI>();
        pointsOfInterest.add(new POI(
                2,
                "Mary Br\u00fcck building",
                "mary-bruck",
                "kb",
                "building",
                -3.1730116009930500,
                55.9231925860886040,
                "Colin MacLaurin Road",
                "King's Buildings",
                "Edinburgh",
                "EH9 3DW"
        ));
        pointsOfInterest.add(new POI(
                4,
                "St Cecilia's Hall",
                "st-cecilias-hall",
                "central",
                "building",
                -3.1864374876022340,
                55.9488904424293240,
                "Niddry Street",
                "Cowgate",
                "Edinburgh",
                "EH1 1NQ"
        ));
        pointsOfInterest.add(new POI(
                9,
                "Edinburgh Centre for Carbon Innovation (ECCI)",
                "ecci",
                "central",
                "building",
                -3.1840935499349143,
                55.9486009337296800,
                "High School Yards",
                "Infirmary Street",
                "Edinburgh",
                "EH1 1LZ"
        ));
        pointsOfInterest.add(new POI(
                10,
                "George Square (7)",
                "7-george-sq",
                "building",
                -3.1887418181577230,
                55.9444705808318500,
                "7 George Square",
                "Edinburgh",
                "EH8 9JZ"
        ));
        pointsOfInterest.add(new POI(
                11,
                "Student Disability Service",
                "disability-office",
                "central",
                "building",
                -3.1888836622238160,
                55.9427473673666640,
                "Main University Library",
                "30 George Square",
                "Edinburgh",
                "EH8 9LJ"
        ));
        pointsOfInterest.add(new POI(
                12,
                "Alison House",
                "alison-house",
                "central",
                "building",
                -3.1864804029464720,
                55.9461599733836400,
                "12 Nicolson Square",
                "Edinburgh",
                "EH8 9DF"
        ));
        pointsOfInterest.add(new POI(
                13,
                "Potterrow",
                "potterrow",
                "central",
                "building",
                -3.1879234313964844,
                55.9461719890707060,
                "5/2 Bristo Square",
                "Edinburgh",
                "EH8 9AL"
        ));
        pointsOfInterest.add(new POI(
                14,
                "Business School",
                "business-school",
                "central",
                "building",
                -3.1873118877410890,
                55.9431078681228900,
                "29 Buccleuch Place",
                "Edinburgh",
                "EH8 9JS"
        ));
        pointsOfInterest.add(new POI(
                15,
                "Bedlam Theatre",
                "bedlam-theatre",
                "central",
                "building",
                -3.1908202171325684,
                55.9463762551806900,
                "11b Bristo Place",
                "Edinburgh",
                "EH1 1EZ"
        ));
        pointsOfInterest.add(new POI(
                16,
                "Medical School (Old Medical School)",
                "medical-school",
                "central",
                "building",
                -3.1905627250671387,
                55.9452347542822000,
                "Teviot Place",
                "Edinburgh",
                "EH8 9AG"
        ));
        pointsOfInterest.add(new POI(
                17,
                "McEwan Hall",
                "mcewan-hall",
                "central",
                "building",
                -3.1896400451660156,
                55.9453789457260800,
                "Teviot Place",
                "Edinburgh",
                "EH8 9AG"
        ));
        pointsOfInterest.add(new POI(
                18,
                "Reid Concert Hall",
                "reid-concert-hall",
                "central",
                "building",
                -3.1894040107727050,
                55.9448983054919800,
                "Bristo Square",
                "Edinburgh",
                "EH8 9AL"
        ));
        pointsOfInterest.add(new POI(
                19,
                "Teviot Row House",
                "teviot-row-house",
                "central",
                "building",
                -3.1889533996582030,
                55.9449583858474800,
                "Bristo Square",
                "Edinburgh",
                "EH8 9AL"
        ));
        pointsOfInterest.add(new POI(
                20,
                "Hugh Robson Building",
                "hugh-robson-building",
                "central",
                "building",
                -3.1899189949035645,
                55.9442794724061900,
                "George Square",
                "Edinburgh",
                "EH8 9XD"
        ));
        pointsOfInterest.add(new POI(
                21,
                "Edinburgh Global (formerly known as International Office)",
                "edinburgh-global",
                "central",
                "building",
                -3.1854477524757385,
                55.9431904824069100,
                "33 Buccluech Place",
                "Edinburgh",
                "EH8 9JS"
        ));
        pointsOfInterest.add(new POI(
                22,
                "Appleton Tower",
                "appleton-tower",
                "central",
                "building",
                -3.1870651245117188,
                55.9444416917442200,
                "11 Crichton Street",
                "Edinburgh",
                "EH8 9LE"
        ));
        pointsOfInterest.add(new POI(
                23,
                "College of Arts, Humanities and Social Sciences Office",
                "college-of-arts-humanities-and-social-sciences-office",
                "central",
                "building",
                -3.1872582435607910,
                55.9439970889746000,
                "55-56 George Square",
                "Edinburgh",
                "EH8 9JU"
        ));
        pointsOfInterest.add(new POI(
                24,
                "George Square (50)",
                "50-george-square",
                "central",
                "building",
                -3.1871938705444336,
                55.9438408759789600,
                "50 George Square",
                "Edinburgh",
                "EH8 9LH"
        ));
        pointsOfInterest.add(new POI(
                25,
                "David Hume Tower Lecture Theatres",
                "david-hume-tower-lecture-theatres",
                "central",
                "building",
                -3.1862711906433105,
                55.9435644975969700,
                "George Square",
                "Edinburgh",
                "EH8 9LX"
        ));
        pointsOfInterest.add(new POI(
                26,
                "David Hume Tower",
                "david-hume-tower",
                "central",
                "building",
                -3.1866574287414550,
                55.9432040010911400,
                "George Square",
                "Edinburgh",
                "EH8 9JX"
        ));
        pointsOfInterest.add(new POI(
                27,
                "Student Counselling Service",
                "student-counselling-service",
                "central",
                "building",
                -3.1886690855026245,
                55.9427804134090100,
                "Main University Library",
                "30 George Square",
                "Edinburgh",
                "EH8 9LJ"
        ));
        pointsOfInterest.add(new POI(
                28,
                "Careers Service (Central Area)",
                "careers-service",
                "central",
                "building",
                -3.1894576549530030,
                55.9426602458471200,
                "Main University Library",
                "30 George Square",
                "Edinburgh",
                "EH8 9LJ"
        ));
        pointsOfInterest.add(new POI(
                29,
                "Open Learning, Centre for",
                "office-of-lifelong-learning",
                "central",
                "building",
                -3.1795978546142580,
                55.9501339585784900,
                "Paterson's Land",
                "Holyrood Road",
                "Edinburgh",
                "EH8 8AQ"
        ));
        pointsOfInterest.add(new POI(
                30,
                "International Students Centre",
                "international-students-centre",
                "central",
                "building",
                -3.1877517700195312,
                55.9424709811810700,
                "22b Buccleuch Place",
                "Edinburgh",
                "EH8 9LN"
        ));
        pointsOfInterest.add(new POI(
                31,
                "Main University Library",
                "main-university-library",
                "central",
                "building",
                -3.1891465187072754,
                55.9427113171064800,
                "30 George Square",
                "Edinburgh",
                "EH8 9LJ"
        ));
        pointsOfInterest.add(new POI(
                32,
                "George Square Lecture Theatre",
                "george-square-lecture-theatre",
                "central",
                "building",
                -3.1882023811340330,
                55.9428795513669100,
                "George Square",
                "Edinburgh",
                "EH8 9LH"
        ));
        pointsOfInterest.add(new POI(
                59,
                "Weir Building",
                "weir-building",
                "kb",
                "building",
                -3.1792438030242920,
                55.9231491035277340,
                "King's Buildings",
                "Max Born Crescent",
                "Edinburgh",
                "EH9 3BF"
        ));
        pointsOfInterest.add(new POI(
                60,
                "Crew Building",
                "crew-building",
                "kb",
                "building",
                -3.1776666641235350,
                55.9230950007645600,
                "King's Buildings",
                "Alexander Crum Brown Road",
                "Edinburgh",
                "EH9 3FF"
        ));
        pointsOfInterest.add(new POI(
                61,
                "Joseph Black Building",
                "joseph-black-building",
                "kb",
                "building",
                -3.1762075424194336,
                55.9236600703440500,
                "David Brewster Road",
                "King's Buildings",
                "Edinburgh",
                "EH9 3FJ"
        ));
        pointsOfInterest.add(new POI(
                62,
                "Roger Land Building",
                "roger-land-building",
                "kb",
                "building",
                -3.1764221191406250,
                55.9223014848946700,
                "Alexander Crum Brown Road",
                "King's Buildings",
                "Edinburgh",
                "EH9 3FF"
        ));
        pointsOfInterest.add(new POI(
                63,
                "Peter Wilson Building",
                "peter-wilson-building",
                "kb",
                "building",
                -3.1765186786651610,
                55.9216221743117100,
                "Nicholas Kemmer Road",
                "King's Buildings",
                "Edinburgh",
                "EH9 3FH"
        ));
        pointsOfInterest.add(new POI(
                64,
                "Grant Institute",
                "grant-institute",
                "kb",
                "building",
                -3.1746411323547363,
                55.9238464212712600,
                "James Hutton Road",
                "King's Buildings",
                "Edinburgh",
                "EH9 3FE"
        ));
        pointsOfInterest.add(new POI(
                65,
                "King's Buildings House",
                "kings-buildings-house",
                "kb",
                "building",
                -3.1748771667480470,
                55.9235759115671200,
                "KB Square",
                "King's Buildings",
                "Edinburgh",
                "EH9 3DL"
        ));
        pointsOfInterest.add(new POI(
                66,
                "King's Buildings Centre",
                "kings-buildings-centre",
                "kb",
                "building",
                -3.1746411323547363,
                55.9229447149150500,
                "Thomas Bayes Road",
                "King's Buildings",
                "Edinburgh",
                "EH9 3FG"
        ));
        pointsOfInterest.add(new POI(
                67,
                "James Clerk Maxwell Building",
                "james-clerk-maxwell-building",
                "kb",
                "building",
                -3.1740939617156982,
                55.9216702674879260,
                "Peter Guthrie Tait Road",
                "King's Buildings",
                "Edinburgh",
                "EH9 3FD"
        ));
        pointsOfInterest.add(new POI(
                68,
                "Ashworth Laboratories",
                "ashworth-laboratories",
                "kb",
                "building",
                -3.1727099418640137,
                55.9239606359128100,
                "Charlotte Auerbach Road",
                "King's Buildings",
                "Edinburgh",
                "EH9 3FL"
        ));
        pointsOfInterest.add(new POI(
                69,
                "Sanderson Building",
                "sanderson-building",
                "kb",
                "building",
                -3.1717228889465330,
                55.9231010121864360,
                "Robert Stevenson Road",
                "King's Buildings",
                "Edinburgh",
                "EH9 3FB"
        ));
        pointsOfInterest.add(new POI(
                70,
                "John Muir Building",
                "john-muir-building",
                "kb",
                "building",
                -3.1732544302940370,
                55.9227328108771200,
                "Colin Maclaurin Road ",
                "King's Buildings",
                "Edinburgh",
                "EH9 3DW"
        ));
        pointsOfInterest.add(new POI(
                71,
                "Hudson Beare Building",
                "hudson-beare-building",
                "kb",
                "building",
                -3.1713366508483887,
                55.9225239114362440,
                "Colin Maclaurin Road",
                "King's Buildings",
                "Edinburgh",
                "EH9 3DW"
        ));
        pointsOfInterest.add(new POI(
                72,
                "Alrick Building",
                "alrick-building",
                "kb",
                "building",
                -3.1714224815368652,
                55.9221632191038440,
                "Max Born Crescent",
                "King's Buildings",
                "Edinburgh",
                "EH9 3BF"
        ));
        pointsOfInterest.add(new POI(
                73,
                "Daniel Rutherford Building",
                "daniel-rutherford-building",
                "kb",
                "building",
                -3.1706821918487550,
                55.9219287672878450,
                "Max Born Crescent",
                "King's Buildings",
                "Edinburgh",
                "EH9 3BF"
        ));
        pointsOfInterest.add(new POI(
                74,
                "Darwin Building",
                "darwin-building",
                "kb",
                "building",
                -3.1710684299468994,
                55.9214177776469500,
                "Max Born Crescent",
                "King's Buildings",
                "Edinburgh",
                "EH9 3BF"
        ));
        pointsOfInterest.add(new POI(
                75,
                "Swann Building",
                "michael-swann-building",
                "kb",
                "building",
                -3.1719267368316650,
                55.9216101510083200,
                "Max Born Crescent",
                "King's Buildings",
                "Edinburgh",
                "EH9 3BF"
        ));
        pointsOfInterest.add(new POI(
                76,
                "John Murray Building",
                "john-murray-labs",
                "kb",
                "building",
                -3.1734287738800050,
                55.9234316389528840,
                "James Hutton Road",
                "King's Buildings",
                "Edinburgh",
                "EH9 3FE"
        ));
        pointsOfInterest.add(new POI(
                77,
                "March Building",
                "march-building",
                "kb",
                "building",
                -3.1728494167327880,
                55.9234436616912500,
                "James Hutton Road",
                "King's Buildings",
                "Edinburgh",
                "EH9 3FE"
        ));
        pointsOfInterest.add(new POI(
                78,
                "Ann Walker Building",
                "ann-walker-building",
                "kb",
                "building",
                -3.1738901138305664,
                55.9231070236073500,
                "Thomas Bayes Road",
                "King's Buildings",
                "Edinburgh",
                "EH9 3FG"
        ));
        pointsOfInterest.add(new POI(
                79,
                "Structures Lab",
                "structures-lab",
                "kb",
                "building",
                -3.1726777553558350,
                55.9229086462244400,
                "Colin Maclaurin Road",
                "King's Buildings",
                "Edinburgh",
                "EH9 3DW"
        ));
        pointsOfInterest.add(new POI(
                80,
                "Fleeming Jenkin Building",
                "fleeming-jenkin-building",
                "kb",
                "building",
                -3.1723880767822266,
                55.9224096925608100,
                "Colin Maclaurin Road",
                "King's Buildings",
                "Edinburgh",
                "EH9 3DW"
        ));
        pointsOfInterest.add(new POI(
                81,
                "Alexander Graham Bell Building",
                "alexander-graham-bell-building",
                "kb",
                "building",
                -3.1727340817451477,
                55.9222684213808400,
                "King's Buildings",
                "Thomas Bayes Road",
                "Edinburgh",
                "EH9 3FG"
        ));
        pointsOfInterest.add(new POI(
                85,
                "Pleasance",
                "pleasance",
                "central",
                "building",
                -3.1815088394796476,
                55.9478375275095800,
                "Pleasance",
                "Edinburgh",
                "EH8 9TJ"
        ));
        pointsOfInterest.add(new POI(
                102,
                "Pollock Halls of Residence",
                "pollock",
                "building",
                -3.1718409061431885,
                55.9403791945471550,
                "18 Holyrood Park Road",
                "Edinburgh",
                "EH16 5AY"
        ));
        pointsOfInterest.add(new POI(
                104,
                "Abden House",
                "abden-house",
                "building",
                -3.1692230701446533,
                55.9374401075255800,
                "1 Marchhall Crescent",
                "Edinburgh",
                "EH16 5HP"
        ));
        pointsOfInterest.add(new POI(
                106,
                "South College Street",
                "south-college-street",
                "central",
                "building",
                -3.1870731711387634,
                55.9469785335416740,
                "South College Street",
                "Edinburgh",
                "EH8 9AA"
        ));
        pointsOfInterest.add(new POI(
                107,
                "George Square",
                "george-square",
                "central",
                "building",
                -3.1886315345764160,
                55.9435284480973960,
                "George Square",
                "Edinburgh",
                "EH8 9JZ"
        ));
        pointsOfInterest.add(new POI(
                108,
                "Informatics Forum",
                "informatics-forum",
                "central",
                "building",
                -3.1875371932983400,
                55.9446652929679200,
                "10 Crichton Street",
                "Edinburgh",
                "EH8 9AB"
        ));
        pointsOfInterest.add(new POI(
                109,
                "Dugald Stewart Building",
                "dugald-stewart-building",
                "central",
                "building",
                -3.1880253553390503,
                55.9454119895230000,
                "3 Charles Street",
                "Edinburgh",
                "EH8 9AD"
        ));
        pointsOfInterest.add(new POI(
                111,
                "Faraday Building",
                "faraday-building",
                "kb",
                "building",
                -3.1716692447662354,
                55.9223360512652800,
                "Colin Maclaurin Road",
                "King's Buildings",
                "Edinburgh",
                "EH9 3DW"
        ));
        pointsOfInterest.add(new POI(
                112,
                "Chancellor's Building",
                "chancellors-building",
                "littlefrance",
                "building",
                -3.1368756294250490,
                55.9220181396980500,
                "49 Little France Crescent",
                "Edinburgh",
                "EH16 4SB"
        ));
        pointsOfInterest.add(new POI(
                113,
                "Queen's Medical Research Institute",
                "qmri",
                "littlefrance",
                "building",
                -3.1388711929321290,
                55.9222413693940400,
                "47 Little France Crescent",
                "Edinburgh",
                "EH16 4TJ"
        ));
        pointsOfInterest.add(new POI(
                114,
                "Chrystal Macmillan Building",
                "chrystal-macmillan-building",
                "central",
                "building",
                -3.1908416748046875,
                55.9441765328811300,
                "15a George Square",
                "Edinburgh",
                "EH8 9LD"
        ));
        pointsOfInterest.add(new POI(
                115,
                "Visitor Centre",
                "visitor-centre",
                "central",
                "building",
                -3.1877946853637695,
                55.9450867572402600,
                "2 Charles Street",
                "Edinburgh",
                "EH8 9AD"
        ));
        pointsOfInterest.add(new POI(
                116,
                "Talbot Rice Gallery",
                "talbot-rice-gallery",
                "central",
                "building",
                -3.1878644227981567,
                55.9471933088459540,
                "Old College",
                "South Bridge",
                "Edinburgh",
                "EH8 9YL"
        ));
        pointsOfInterest.add(new POI(
                117,
                "Charles Stewart House",
                "charles-stewart-house",
                "central",
                "building",
                -3.1879770755767822,
                55.9479614837865200,
                "9-16 Chambers Street",
                "Edinburgh",
                "EH1 1HR"
        ));
        pointsOfInterest.add(new POI(
                118,
                "Royal (Dick) School of Veterinary Studies",
                "dick-vet-easter-bush",
                "easter",
                "building",
                -3.2010340690612793,
                55.8658360390777300,
                "Easter Bush",
                "Midlothian",
                "EH25 9RG"
        ));
        pointsOfInterest.add(new POI(
                119,
                "Buccleuch Place",
                "buccleuch-place",
                "central",
                "building",
                -3.1865286827087402,
                55.9428735430130360,
                "Buccleuch Place",
                "Edinburgh",
                "EH8 9JS"
        ));
        pointsOfInterest.add(new POI(
                120,
                "Erskine Williamson Building",
                "erskine-williamson-building",
                "kb",
                "building",
                -3.1745553016662598,
                55.9213997425953900,
                "Peter Guthrie Tait Road",
                "King's Buildings",
                "Edinburgh",
                "EH9 3FD"
        ));
        pointsOfInterest.add(new POI(
                121,
                "Health Centre",
                "health-centre",
                "central",
                "building",
                -3.1881996989250183,
                55.9458580780220500,
                "6 Bristo Square",
                "Edinburgh",
                "EH8 9AL"
        ));
        pointsOfInterest.add(new POI(
                122,
                "William Rankine Building",
                "william-rankine-building",
                "kb",
                "building",
                -3.1731122732162476,
                55.9225299229466700,
                "Thomas Bayes Road",
                "King's Buildings",
                "Edinburgh",
                "EH9 3FG"
        ));
        pointsOfInterest.add(new POI(
                126,
                "Charteris Land",
                "charteris-land",
                "central",
                "building",
                -3.1809121370315550,
                55.9501515797825100,
                "Holyrood Road",
                "Edinburgh",
                "EH8 8AQ"
        ));
        pointsOfInterest.add(new POI(
                127,
                "Simon Laurie House",
                "simon-laurie-house",
                "central",
                "building",
                -3.1815370917320250,
                55.9509011741071300,
                "186 -198 Canongate",
                "Edinburgh",
                "EH8 8AQ"
        ));
        pointsOfInterest.add(new POI(
                128,
                "St John's Land",
                "st-johns-land",
                "central",
                "building",
                -3.1810247898101807,
                55.9508486118686200,
                "Holyrood",
                "Edinburgh",
                "EH8 8AQ"
        ));
        pointsOfInterest.add(new POI(
                129,
                "Dalhousie Land",
                "dalhousie-land",
                "central",
                "building",
                -3.1807351112365723,
                55.9506295517108000,
                "Holyrood",
                "Edinburgh",
                "EH8 8AQ"
        ));
        pointsOfInterest.add(new POI(
                130,
                "Old Nursery School Building",
                "nursery",
                "central",
                "building",
                -3.1806653738021850,
                55.9504253080368300,
                "Holyrood",
                "Edinburgh",
                "EH8 8AQ"
        ));
        pointsOfInterest.add(new POI(
                131,
                "Thomson's Land",
                "thomsons-land",
                "central",
                "building",
                -3.1800457835197450,
                55.9507765263969360,
                "Holyrood Road",
                "Edinburgh",
                "EH8 8AQ"
        ));
        pointsOfInterest.add(new POI(
                132,
                "Moray House Reception / Old Moray House",
                "moray-house-reception",
                "central",
                "building",
                -3.1804883480072020,
                55.9510920995776900,
                "Holyrood Road",
                "Edinburgh",
                "EH8 8AQ"
        ));
        pointsOfInterest.add(new POI(
                133,
                "Roslin Institute",
                "roslin-institute",
                "easter",
                "building",
                -3.1981158256530760,
                55.8654627786743400,
                "Easter Bush",
                "Midlothian",
                "EH25 9RG"
        ));
        pointsOfInterest.add(new POI(
                134,
                "Procurement Office incorporating Printing Services",
                "procurement-office",
                "central",
                "building",
                -3.1851929426193237,
                55.9483617857850800,
                "13 Infirmary Street",
                "Edinburgh",
                "EH1 1LT"
        ));
        pointsOfInterest.add(new POI(
                135,
                "MacKenzie Medical Centre",
                "mackenzie-medical-centre",
                "central",
                "building",
                -3.1835299730300903,
                55.9455922279190640,
                "Levinson House",
                "20 West Richmond Street",
                "Edinburgh",
                "EH8 9DX"
        ));
        pointsOfInterest.add(new POI(
                136,
                "Roxburgh Street (1)",
                "roxburgh-street",
                "central",
                "building",
                -3.1833046674728394,
                55.9478807820734050,
                "1 Roxburgh Street",
                "Edinburgh",
                "EH8 9TA"
        ));
        pointsOfInterest.add(new POI(
                137,
                "Western General Hospital",
                "western-general",
                "westerngeneral",
                "building",
                -3.2348728179931640,
                55.9620294457321560,
                "Crewe Road South",
                "Edinburgh",
                "EH4 2XU"
        ));
        pointsOfInterest.add(new POI(
                138,
                "Hope Park Square",
                "hope-park",
                "central",
                "building",
                -3.1860485672950745,
                55.9420752097637600,
                "Hope Park Square",
                "Edinburgh",
                "EH8 9NW"
        ));
        pointsOfInterest.add(new POI(
                139,
                "Careers Service (King's Buildings)",
                "kb",
                "building",
                -3.1792491674423218,
                55.9232092176204700,
                "Weir Building",
                "Max Born Crescent",
                "Edinburgh",
                "EH9 3BF"
        ));
        pointsOfInterest.add(new POI(
                140,
                "Peffermill Playing Fields",
                "peffermill",
                "building",
                -3.1566488742828370,
                55.9299714578133160,
                "Laurie Liddell Clubhouse",
                "42 Peffermill Road",
                "Edinburgh",
                "EH16 5LL"
        ));
        pointsOfInterest.add(new POI(
                141,
                "Firbush",
                "firbush",
                "building",
                -4.2703503370285030,
                56.4751287681757600,
                "Killin",
                "Perthshire",
                "FK21 8SU"
        ));
        pointsOfInterest.add(new POI(
                142,
                "Royal Observatory, Edinburgh",
                "royal-observatory",
                "building",
                -3.1872367858886720,
                55.9228601538269600,
                "Blackford Hill",
                "Edinburgh",
                "EH9 3HJ"
        ));
        pointsOfInterest.add(new POI(
                143,
                "Waddington Building",
                "ch-waddington",
                "kb",
                "building",
                -3.1700813770294190,
                55.9216402092597900,
                "Max Born Crescent",
                "King's Buildings",
                "Edinburgh",
                "EH9 3BF"
        ));
        pointsOfInterest.add(new POI(
                144,
                "Labyrinth",
                "labyrinth",
                "central",
                "building",
                -3.1895273923873900,
                55.9438284590276300,
                "George Square",
                "Edinburgh",
                "EH8 9JZ."
        ));
        pointsOfInterest.add(new POI(
                145,
                "Institute for Academic Development",
                "academic-development",
                "central",
                "building",
                -3.1821757704892660,
                55.9498129209811600,
                "1 Morgan Lane",
                "Edinburgh",
                "EH8 8FP"
        ));
        pointsOfInterest.add(new POI(
                146,
                "Royal Edinburgh Hospital",
                "royal-edinburgh-hospital",
                "building",
                -3.2134473323822020,
                55.9278273042274700,
                "Morningside Terrace",
                "Edinburgh",
                "EH10 5HF"
        ));
        pointsOfInterest.add(new POI(
                147,
                "Hospital for Small Animals",
                "small-animals-hospital",
                "easter",
                "building",
                -3.2022893428802490,
                55.8650834942677400,
                "Easter Bush",
                "Midlothian",
                "EH25 9RG"
        ));
        pointsOfInterest.add(new POI(
                149,
                "Infirmary Street (9-11)",
                "Infirmary Steet",
                "central",
                "building",
                -3.1853377819061280,
                55.9483347520127000,
                "9-11 Infirmary Street",
                "Edinburgh",
                "EH1 1NP"
        ));
        pointsOfInterest.add(new POI(
                150,
                "Bristo Square (7)",
                "bristo-square",
                "central",
                "building",
                -3.1881058216094970,
                55.9456913586793300,
                "7 Bristo Square",
                "Edinburgh",
                "EH8 9AL"
        ));
        pointsOfInterest.add(new POI(
                151,
                "John McIntyre Conference Centre",
                "john-mcIntyre-conference-centre",
                "building",
                -3.1700706481933594,
                55.9400090542789300,
                "18 Holyrood Park Road",
                "Edinburgh",
                "EH16 5AY"
        ));
        pointsOfInterest.add(new POI(
                152,
                "Library Annexe",
                "library-annexe",
                "building",
                -3.3035373687744140,
                55.9305003655093700,
                "Unit 1a",
                "20-22 South Gyle Crescent",
                "Edinburgh",
                "EH12 9EB"
        ));
        pointsOfInterest.add(new POI(
                153,
                "Old Surgeons' Hall",
                "old-surgeons-hall",
                "central",
                "building",
                -3.1831464171409607,
                55.9484864412690800,
                "Surgeons Square",
                "High School Yards",
                "Edinburgh",
                "EH1 1LZ"
        ));
        pointsOfInterest.add(new POI(
                154,
                "Old Infirmary (Geography)",
                "institute-of-geography",
                "central",
                "building",
                -3.1837606430053710,
                55.9482334252486500,
                "1 Drummond Street",
                "Edinburgh",
                "EH8 9XP"
        ));
        pointsOfInterest.add(new POI(
                155,
                "Drummond Library (GeoSciences)",
                "drummond-library",
                "central",
                "building",
                -3.1829774379730225,
                55.9486869905629200,
                "High School Yards",
                "Edinburgh",
                "EH1 1LZ"
        ));
        pointsOfInterest.add(new POI(
                156,
                "Chisholm House",
                "chisholm-house",
                "central",
                "building",
                -3.1830418109893800,
                55.9488491914385600,
                "1 Surgeons Square",
                "High School Yards",
                "Edinburgh",
                "EH1 1LZ"
        ));
        pointsOfInterest.add(new POI(
                157,
                "Drummond Street Annex",
                "drummond-street-annex",
                "central",
                "building",
                -3.1829130649566650,
                55.9482574553323400,
                "1 Drummond Street",
                "Edinburgh",
                "EH1 1LZ"
        ));
        pointsOfInterest.add(new POI(
                158,
                "Evolution House",
                "evolution-house",
                "central",
                "building",
                -3.2005995512008667,
                55.9460736605762700,
                "78 Westport",
                "Edinburgh",
                "EH1 2LE"
        ));
        pointsOfInterest.add(new POI(
                159,
                "Hunter Building",
                "hunter-building",
                "central",
                "building",
                -3.1980729103088380,
                55.9451282124570850,
                "Lauriston Place",
                "Edinburgh",
                "EH3 9DF"
        ));
        pointsOfInterest.add(new POI(
                160,
                "ECA Main Building",
                "eca-main-building",
                "central",
                "building",
                -3.1990599632263184,
                55.9458551741833600,
                "Lauriston Place",
                "Edinburgh",
                "EH3 9DF"
        ));
        pointsOfInterest.add(new POI(
                162,
                "William Robertson Wing",
                "william-robertson-wing",
                "central",
                "building",
                -3.1907665729522705,
                55.9450080521798600,
                "Doorway 4",
                "Teviot Place",
                "Edinburgh",
                "EH8 9AG"
        ));
        pointsOfInterest.add(new POI(
                163,
                "Economics, School of",
                "economics",
                "central",
                "building",
                -3.1860029697418213,
                55.9431288972300800,
                "30-31 Buccleuch Place",
                "Edinburgh",
                "EH8 9JT"
        ));
        pointsOfInterest.add(new POI(
                164,
                "Scottish Centre for Regenerative Medicine",
                "SCRM",
                "littlefrance",
                "building",
                -3.1307816505432130,
                55.9213756958468900,
                "Edinburgh BioQuarter",
                "5 Little France Drive",
                "Edinburgh",
                "EH16 4UU"
        ));
        pointsOfInterest.add(new POI(
                165,
                "Edinburgh Imaging Facility (QMRI)",
                "imaging-facility-QMRI",
                "littlefrance",
                "building",
                -3.1395041942596436,
                55.9218746628211960,
                "47 Little France Crescent",
                "Edinburgh",
                "EH16 4TJ"
        ));
        pointsOfInterest.add(new POI(
                166,
                "Riddell-Swan Veterinary Cancer Centre",
                "riddell-swan-building",
                "easter",
                "building",
                -3.2020211219787598,
                55.8647884927243440,
                "Easter Bush",
                "Midlothian",
                "EH25 9RG"
        ));
        pointsOfInterest.add(new POI(
                167,
                "Equine Hospital",
                "equine-hospital",
                "easter",
                "building",
                -3.1992530822753906,
                55.8642948116602700,
                "Easter Bush Campus",
                "Midlothian",
                "EH25 9RG"
        ));
        pointsOfInterest.add(new POI(
                168,
                "Easter Bush Campus Service Centre",
                "easter-campus-management",
                "easter",
                "building",
                -3.1989848613739014,
                55.8665524481898640,
                "Easter Bush",
                "Midlothian",
                "EH25 9RG"
        ));
        pointsOfInterest.add(new POI(
                169,
                "Sir Alexander Robertson Building",
                "alexander-robertson",
                "easter",
                "building",
                -3.1983304023742676,
                55.8648005336474900,
                "Easter Bush",
                "Midlothian",
                "EH25 9RG"
        ));
        pointsOfInterest.add(new POI(
                170,
                "Farm Animal Practice and Equine Clinical Unit",
                "farm-animal-practice",
                "easter",
                "building",
                -3.1991994380950928,
                55.8648607382074200,
                "Easter Bush",
                "Midlothian",
                "EH25 9RG"
        ));
        pointsOfInterest.add(new POI(
                171,
                "Farm Animal Hospital",
                "farm-animal-unit",
                "easter",
                "building",
                -3.1998538970947266,
                55.8641382773298200,
                "Easter Bush",
                "Midlothian",
                "EH25 9RG"
        ));
        pointsOfInterest.add(new POI(
                172,
                "Scintigraphy and Exotic Animal Unit",
                "scintigraphy-exotics",
                "easter",
                "building",
                -3.1989204883575440,
                55.8637529593674600,
                "Easter Bush",
                "Midlothian",
                "EH25 9RG"
        ));
        pointsOfInterest.add(new POI(
                173,
                "Biomedical Research Facility",
                "biomedical-research-facility",
                "westerngeneral",
                "building",
                -3.2382416725158690,
                55.9628822006132100,
                "Western General Hospital",
                "Edinburgh",
                "EH4 2XU"
        ));
        pointsOfInterest.add(new POI(
                174,
                "CJD Surveillance Unit",
                "cjd-surveillance-unit",
                "westerngeneral",
                "building",
                -3.2370507717132570,
                55.9621555585534200,
                "Western General Hospital",
                "Edinburgh",
                "EH4 2XU"
        ));
        pointsOfInterest.add(new POI(
                175,
                "Wellcome Trust Clinical Research Facility",
                "wellcome-trust-clinical-research",
                "westerngeneral",
                "building",
                -3.2354736328125000,
                55.9622036014248400,
                "Western General Hospital",
                "Edinburgh",
                "EH4 2XU"
        ));
        pointsOfInterest.add(new POI(
                176,
                "Clinical Neurosciences",
                "clinical-neurosciences",
                "westerngeneral",
                "building",
                -3.2371473312377930,
                55.9618192567835000,
                "Western General Hospital",
                "Edinburgh",
                "EH4 2XU"
        ));
        pointsOfInterest.add(new POI(
                177,
                "Bramwell Dott Building",
                "bramwell-dott-building",
                "westerngeneral",
                "building",
                -3.2368093729019165,
                55.9613568370782500,
                "Western General Hospital",
                "Edinburgh",
                "EH4 2XU"
        ));
        pointsOfInterest.add(new POI(
                178,
                "Division of Pathology Laboratories",
                "pathology-labs",
                "westerngeneral",
                "building",
                -3.2339179515838623,
                55.9620714833849000,
                "Western General Hospital",
                "Edinburgh",
                "EH4 2XU"
        ));
        pointsOfInterest.add(new POI(
                179,
                "IGMM Complex",
                "igmm-complex",
                "westerngeneral",
                "building",
                -3.2323193550109863,
                55.9622936816480400,
                "Western General Hospital",
                "Edinburgh",
                "EH4 2XU"
        ));
        pointsOfInterest.add(new POI(
                180,
                "Noreen and Kenneth Murray Library",
                "noreen-kenneth-murray-library",
                "kb",
                "building",
                -3.1750380992889404,
                55.9229507263602100,
                "Thomas Bayes Road",
                "King's Buildings",
                "Edinburgh",
                "EH9 3FG"
        ));
        pointsOfInterest.add(new POI(
                181,
                "Anatomical Museum",
                "anatomical-museum",
                "central",
                "building",
                -3.1900537014010410,
                55.9450908404547600,

                "Doorway 3, Medical School",
                "Teviot Place",
                "Edinburgh",
                "EH8 9AG"
        ));
        pointsOfInterest.add(new POI(
                182,
                "Reid Museum of Musical Instruments",
                "reid-museum",
                "central",
                "building",
                -3.1891578435900670,
                55.9449286238358500,
                "Reid Concert Hall",
                "Bristo Square",
                "Edinburgh",
                "EH8 9AL"
        ));
        pointsOfInterest.add(new POI(
                221,
                "Student Recruitment and Admissions",
                "student-recruitment-admissions",
                "central",
                "building",
                -3.1854933500289917,
                55.9431919844831500,

                "33 Buccleuch Place",
                "Edinburgh",
                "EH8 9JT"
        ));
        pointsOfInterest.add(new POI(
                222,
                "George Square (16-22)",
                "16-27-george-square",
                "central",
                "building",
                -3.1905305385589600,
                55.9434923985642300,
                "16-22 George Square",
                "Edinbrugh",
                "EH8 9LD"
        ));
        pointsOfInterest.add(new POI(
                223,
                "George Square (2-15)",
                "1-15-george-square",
                "central",
                "building",
                -3.1889963150024414,
                55.9444476998548050,
                "2-15 George Square",
                "Edinburgh",
                "EH8 9JZ"
        ));
        pointsOfInterest.add(new POI(
                224,
                "George Square (55-60)",
                "55-60-george-square",
                "central",
                "building",
                -3.1872904300689697,
                55.9441773339558350,
                "55-60 George Square",
                "Edinburgh",
                "EH8 9JU"
        ));
        pointsOfInterest.add(new POI(
                225,
                "George Square (1)",
                "1-george-square",
                "central",
                "building",
                -3.1880736351013184,
                55.9445678618707000,
                "1 George Square",
                "Edinburgh",
                "EH8 9JZ"
        ));
        pointsOfInterest.add(new POI(
                226,
                "FloWave Ocean Energy Research Facility",
                "flowave",
                "kb",
                "building",
                -3.1787583231925964,
                55.9221391728290200,

                "Building 29, King's Buildings",
                "Max Born Crescent",
                "Edinburgh",
                "EH9 3BF"
        ));
        pointsOfInterest.add(new POI(
                228,
                "Edinburgh Dental Institute",
                "dental",
                "central",
                "building",
                -3.1972124129561053,
                55.9444931361609100,

                "4th Floor, Lauriston Building",
                "Lauriston Place",
                "Edinburgh",
                "EH3 9HA"
        ));
        pointsOfInterest.add(new POI(
                229,
                "Medical Education Centre",
                "medical-education-centre",
                "westerngeneral",
                "building",
                -3.2344329357147217,
                55.9623867643250750,
                "Western General Hospital",
                "Edinburgh",
                "EH4 2XU"
        ));
        pointsOfInterest.add(new POI(
                235,
                "Arcadia Nursery",
                "arcadia",
                "kb",
                "building",
                -3.1770189106464386,
                55.9243168030941800,
                "Max Born Crescent",
                "Edinburgh",
                "EH9 3BF"
        ));
        pointsOfInterest.add(new POI(
                236,
                "Christina Miller Building",
                "christina-miller",
                "kb",
                "building",
                -3.1762893497943880,
                55.9228830975483100,
                "KB Square",
                "King's Buildings",
                "Edinburgh",
                "EH9 3DL"
        ));
        pointsOfInterest.add(new POI(
                238,
                "St Leonard's Land",
                "st-leonards-land",
                "central",
                "building",
                -3.1796447932720184,
                55.9494235973597600,
                "Holyrood Road",
                "Edinburgh",
                "EH8 8AQ"
        ));
        pointsOfInterest.add(new POI(
                242,
                "Forrest Hill Building",
                "forrest-hill-building",
                "central",
                "building",
                -3.1919199228286743,
                55.9460157848478160,
                "5 Forrest Hill",
                "Edinburgh",
                "EH1 2QL"
        ));
        pointsOfInterest.add(new POI(
                243,
                "Sport and Exercise",
                "centre-sport-exercise",
                "central",
                "building",
                -3.1814534553996054,
                55.9482471864297600,
                "46 Pleasance",
                "Edinburgh",
                "EH8 9TJ"
        ));
        pointsOfInterest.add(new POI(
                245,
                "High School Yards",
                "high-school-yards",
                "central",
                "building",
                -3.1839591264724730,
                55.9486696690834200,
                "High School Yards",
                "Edinburgh",
                "EH1 1LZ"
        ));
        pointsOfInterest.add(new POI(
                246,
                "North-East Studio Building",
                "north-east-studio",
                "central",
                "building",
                -3.1977570356684737,
                55.9459624149394800,
                "Lauriston Place",
                "Edinburgh",
                "EH3 9DF"
        ));
        pointsOfInterest.add(new POI(
                247,
                "Paterson's Land",
                "patersons-land",
                "central",
                "building",
                -3.1797011196613310,
                55.9500919079601800,
                "Holyrood Road",
                "Edinburgh",
                "EH8 8AQ"
        ));
        pointsOfInterest.add(new POI(
                265,
                "Wilkie Building",
                "wilkie",
                "central",
                "building",
                -3.1891815438893900,
                55.9447400183455340,
                "Medical School",
                "22 - 23 Teviot Row",
                "Edinburgh",
                "EH8 9AG"
        ));
        pointsOfInterest.add(new POI(
                267,
                "George Square (27-29)",
                "27-29-george-sq",
                "central",
                "building",
                -3.1902951300253335,
                55.9430755233431750,
                "27-29 George Square",
                "Edinburgh",
                "EH8 9LD"
        ));
        pointsOfInterest.add(new POI(
                268,
                "Buccleuch Place (17-25)",
                "17-25-buccleuch",
                "central",
                "building",
                -3.1878757776576094,
                55.9424656737581000,
                "17-25 Buccleuch Place",
                "Edinburgh",
                "EH8 9LN"
        ));
        pointsOfInterest.add(new POI(
                269,
                "Buccleuch Place (14-16)",
                "14-16-buccleuch",
                "central",
                "building",
                -3.1867445111493000,
                55.9426225932921140,
                "14-16 Buccleuch Place",
                "Edinburgh",
                "EH8 9LN"
        ));
        pointsOfInterest.add(new POI(
                270,
                "Chaplaincy",
                "chaplaincy",
                "central",
                "building",
                -3.1882885248342063,
                55.9462294139965200,
                "1 Bristo Square",
                "Edinburgh",
                "EH8 9AL"
        ));
        pointsOfInterest.add(new POI(
                273,
                "Old Kirk",
                "old-kirk",
                "central",
                "building",
                -3.1805157959570350,
                55.9497682168029700,
                "37 Holyrood Rd",
                "Edinburgh",
                "EH8 8AQ"
        ));
        pointsOfInterest.add(new POI(
                274,
                "Outreach Centre",
                "outreach",
                "central",
                "building",
                -3.1815886795629920,
                55.9495279246704600,
                "9C Holyrood Road",
                "Edinburgh",
                "EH8 8FP"
        ));
        pointsOfInterest.add(new POI(
                276,
                "Biospace",
                "biospace",
                "kb",
                "building",
                -3.1787515282849200,
                55.9224952565814800,
                "Max Born Crescent",
                "Edinburgh",
                "EH9 3BF"
        ));
        pointsOfInterest.add(new POI(
                277,
                "UK Biochar Research Centre",
                "biochar-research",
                "kb",
                "building",
                -3.1784296632031330,
                55.9231865746809500,
                "Max Born Crescent",
                "Edinburgh",
                "EH9 3BF"
        ));
        pointsOfInterest.add(new POI(
                278,
                "Anne Rowling Regenerative Neurology Clinic",
                "regenerative-neurology",
                "littlefrance",
                "building",
                -3.1377825290019246,
                55.9218908679254340,
                "49 Little France Crescent",
                "Edinburgh",
                "EH16 4SB"
        ));
        pointsOfInterest.add(new POI(
                279,
                "Minto House",
                "minto-house",
                "central",
                "building",
                -3.1890824587026145,
                55.9477839098566900,
                "20-22 Chambers Street",
                "Edinburgh",
                "EH1 1HT"
        ));
        pointsOfInterest.add(new POI(
                281,
                "Old College",
                "old-college",
                "central",
                "building",
                -3.1864655617391690,
                55.9475371471338900,
                "South Bridge",
                "Edinburgh",
                "EH8 9YL"
        ));
        pointsOfInterest.add(new POI(
                282,
                "Institute for Advanced Studies in the Humanities",
                "iash",
                "central",
                "building",
                -3.1859871894175740,
                55.9421498800494850,
                "Hope Park Square",
                "Edinburgh",
                "EH8 9NW"
        ));
        pointsOfInterest.add(new POI(
                284,
                "Scottish Microelectronic Centre",
                "scottish-micro-electronic-centre",
                "kb",
                "building",
                -3.1778985858181840,
                55.9221375697679600,
                "Alexander Crum Brown Road",
                "Edinburgh",
                "EH9 3FF"
        ));
        pointsOfInterest.add(new POI(
                285,
                "Argyle House",
                "argyle",
                "central",
                "building",
                -3.2010186018305830,
                55.9465782172433200,
                "3 Lady Lawson Street",
                "Edinburgh",
                "EH3 9DR"
        ));
        pointsOfInterest.add(new POI(
                287,
                "Psychology Building",
                "psychology-building",
                "central",
                "building",
                -3.1886053385096600,
                55.9444754372987800,
                "7 George Square",
                "Edinburgh",
                "EH8 9JZ"
        ));
        pointsOfInterest.add(new POI(
                298,
                "New College",
                "new-college",
                "central",
                "building",
                -3.1952270865440370,
                55.9497044393992840,
                "Mound Place",
                "Edinburgh",
                "EH1 2LX"
        ));
        pointsOfInterest.add(new POI(
                299,
                "Edinburgh BioQuarter (9) - Usher Institute",
                "nine-bioquarter",
                "littlefrance",
                "building",
                -3.1296175718307495,
                55.9196563146633640,
                "9 Little France Road",
                "Edinburgh",
                "EH16 4UX"
        ));
        pointsOfInterest.add(new POI(
                303,
                "Edinburgh Centre for Professional Legal Studies",
                "ecpls",
                "central",
                "building",
                -3.1818227469921110,
                55.9495084509180400,
                "9B Holyrood Road",
                "Edinburgh",
                "EH8 8FP"
        ));
        pointsOfInterest.add(new POI(
                305,
                "Edinburgh Imaging Facility RIE",
                "imaging-facility-RIE",
                "littlefrance",
                "building",
                -3.1362640857696533,
                55.9210540691517300,
                "57 Little France Crescent",
                "Edinburgh",
                "EH16 4TJ"
        ));
        pointsOfInterest.add(new POI(
                306,
                "Adam House",
                "adam-house",
                "central",
                "building",
                -3.1873333454132080,
                55.9480794321204600,
                "3 Chambers Street",
                "Edinburgh",
                "EH1 1HR"
        ));
        pointsOfInterest.add(new POI(
                307,
                "Edinburgh BioQuarter (9A) - Centre for Dementia Prevention",
                "nine-a-bioquarter",
                "littlefrance",
                "building",
                -3.1286332011222840,
                55.9202845589381400,
                "9a Little France Road",
                "Edinburgh",
                "EH16 4UX"
        ));
        pointsOfInterest.add(new POI(
                308,
                "Lister Learning And Teaching Centre",
                "lister-learning-and-teaching-centre",
                "central",
                "building",
                -3.1840020418167114,
                55.9468989312032360,
                "5 Roxburgh Place",
                "Edinburgh",
                "EH8 9SU"
        ));
        pointsOfInterest.add(new POI(
                309,
                "Lauriston Fire Station",
                "lauriston-fire-station",
                "central",
                "building",
                -3.1988427042961120,
                55.9452227383043100,
                "78 Lauriston Place",
                "Edinburgh",
                "EH3 9DE"
        ));
        pointsOfInterest.add(new POI(
                310,
                "Moray House Lodge",
                "moray-house-lodge",
                "central",
                "building",
                -3.1789822876453400,
                55.9501069260433700,
                "Moray House Lodge",
                "Holyrood Road",
                "Edinburgh",
                "EH8 8AQ"
        ));
    }
}

