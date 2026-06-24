package com.digitalprimaryschool.digitalprimaryschool.model;

public enum Nationnalite {
    CAMEROUNAISE("Camerounaise"),
    FRANCAISE("Française"),
    ANGLAISE("Anglaise"),
    AMERICAINE("Américaine"),
    CANADIENNE("Canadienne"),
    BELGE("Belge"),
    SUISSE("Suisse"),
    ALLEMANDE("Allemande"),
    ITALIENNE("Italienne"),
    ESPAGNOLE("Espagnole"),
    PORTUGAISE("Portugaise"),
    NIGERIANE("Nigériane"),
    SENEGALAISE("Sénégalaise"),
    IVOIRIENNE("Ivoirienne"),
    MALIENNE("Malienne"),
    GABONAISE("Gabonaise"),
    CONGOLAISE("Congolaise"),
    TCHADIENNE("Tchadienne"),
    CENTRAFRICAINE("Centrafricaine"),
    BURKINABE("Burkinabè"),
    BENINOISE("Béninoise"),
    TOGOLAISE("Togolaise"),
    GHANEENNE("Ghanéenne"),
    LIBERIENNE("Libérienne"),
    SIERRA_LEONAISE("Sierra-Léonaise"),
    MAURITANIENNE("Mauritanienne"),
    NIGERIENNE("Nigérienne"),
    SOUDANAISE("Soudanaise"),
    EGYPTIENNE("Égyptienne"),
    MAROCAINE("Marocaine"),
    ALGERIENNE("Algérienne"),
    TUNISIENNE("Tunisienne"),
    LIBYENNE("Libyenne"),
    CHINOISE("Chinoise"),
    INDIENNE("Indienne"),
    PAKISTANAISE("Pakistanaise"),
    LIBANAISE("Libanaise"),
    TURQUE("Turque"),
    RUSSE("Russe"),
    UKRAINIENNE("Ukrainienne"),
    BRESILIENNE("Brésilienne"),
    MEXICAINE("Mexicaine"),
    COLOMBIENNE("Colombienne"),
    PERUVIENNE("Péruvienne"),
    ARGENTINE("Argentine"),
    VENEZUELIENNE("Vénézuélienne"),
    AUSTRALIENNE("Australienne"),
    NEERLANDAISE("Néerlandaise"),
    SUEDOISE("Suédoise"),
    NORVEGIENNE("Norvégienne"),
    DANOISE("Danoise"),
    FINLANDAISE("Finlandaise"),
    GRECQUE("Grecque"),
    ROUMAINE("Roumaine"),
    BULGARE("Bulgare"),
    POLONAISE("Polonaise"),
    TCHEQUE("Tchèque"),
    HONGROISE("Hongroise"),
    AUTRICHIENNE("Autrichienne"),
    IRLANDAISE("Irlandaise"),
    ECOSSAISE("Écossaise"),
    WELSH("Galloise"),
    NEO_ZELANDAISE("Néo-Zélandaise"),
    SUD_AFRICAINE("Sud-Africaine"),
    ANGOLAISE("Angolaise"),
    MOZAMBICAINE("Mozambicaine"),
    ZIMBABWEENNE("Zimbabwéenne"),
    MALAWITE("Malawite"),
    ZAMBIENNE("Zambienne"),
    OUGANDAISE("Ougandaise"),
    KENYANE("Kényane"),
    TANZANIENNE("Tanzanienne"),
    RWANDAISE("Rwandaise"),
    BURUNDAISE("Burundaise"),
    SOMALIENNE("Somalienne"),
    ETHIOPIENNE("Éthiopienne"),
    ERYTHREENNE("Érythréenne"),
    DJIBOUTIENNE("Djiboutienne"),
    COMORIENNE("Comorienne"),
    MALGACHE("Malgache"),
    SEYCHELLOISE("Seychelloise"),
    MAURICIENNE("Mauricienne"),
    AUTRE("Autre");

    private final String libelle;

    Nationnalite(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}