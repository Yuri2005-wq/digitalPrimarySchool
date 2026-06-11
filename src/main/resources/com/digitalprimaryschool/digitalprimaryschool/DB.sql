/*==============================================================*/
/* Script Base de Données École Primaire Privée (Cameroun)       */
/* Spécifiquement adapté, réordonné et optimisé pour SQLite      */
/*==============================================================*/

-- Activation stricte du support des clés étrangères dans SQLite
PRAGMA foreign_keys = ON;

-- Suppression des tables dans l'ordre inverse des dépendances pour éviter les blocages
DROP TABLE IF EXISTS association7;
DROP TABLE IF EXISTS association6;
DROP TABLE IF EXISTS paiementTranche;
DROP TABLE IF EXISTS Paiement;
DROP TABLE IF EXISTS Inscription;
DROP TABLE IF EXISTS Notes;
DROP TABLE IF EXISTS Matiere;
DROP TABLE IF EXISTS Enseignant;
DROP TABLE IF EXISTS Trimestre;
DROP TABLE IF EXISTS Sequence;
DROP TABLE IF EXISTS Bulletin;
DROP TABLE IF EXISTS certificat_scolarite;
DROP TABLE IF EXISTS fiche_renseignement;
DROP TABLE IF EXISTS recue_paiement;
DROP TABLE IF EXISTS DocumentScolaire;
DROP TABLE IF EXISTS TarisScolaire;
DROP TABLE IF EXISTS Classe;
DROP TABLE IF EXISTS Eleve;
DROP TABLE IF EXISTS Parent;
DROP TABLE IF EXISTS Econome;
DROP TABLE IF EXISTS Utilisateur;
DROP TABLE IF EXISTS AnneeScolaire;
DROP TABLE IF EXISTS Tranche;

/*==============================================================*/
/* [NIVEAU 1] Tables totalement indépendantes                   */
/*==============================================================*/

CREATE TABLE AnneeScolaire
(
    idAnnescolaire        TEXT NOT NULL,
    libelle               TEXT,
    dateDebut             TEXT,
    dateFin               TEXT,
    estActive             INTEGER, -- 0 = False, 1 = True
    PRIMARY KEY (idAnnescolaire)
);

CREATE TABLE Tranche
(
    idTranche             TEXT NOT NULL,
    libelleTranche        TEXT,
    montantTranche        REAL,
    dateEcheance          TEXT,
    estPaye               INTEGER DEFAULT 0,
    PRIMARY KEY (idTranche)
);

CREATE TABLE Utilisateur
(
    idUtilisateur         TEXT NOT NULL,
    username              TEXT,
    motDePasseUtilisateur TEXT,
    role                  TEXT,
    is_synced             INTEGER DEFAULT 0,
    derniere_modification TEXT DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (idUtilisateur)
);

CREATE TABLE Parent
(
    idParent              TEXT NOT NULL,
    prenom                TEXT,
    contactParent         INTEGER,
    emailParent           TEXT,
    profession            TEXT,
    adresse               TEXT,
    is_synced             INTEGER DEFAULT 0,
    date_creation         TEXT DEFAULT CURRENT_TIMESTAMP,
    derniere_modification TEXT DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (idParent)
);

CREATE TABLE Classe
(
    idClasse              TEXT NOT NULL,
    nom                   TEXT,
    niveau                TEXT,
    capaciteMax           INTEGER,
    section               TEXT,
    PRIMARY KEY (idClasse) -- Virgule en trop supprimée ici
);

/*==============================================================*/
/* [NIVEAU 2] Tables dépendantes du Niveau 1                     */
/*==============================================================*/

CREATE TABLE Econome
(
    idUtilisateur         TEXT NOT NULL,
    idEconome             TEXT NOT NULL,
    PRIMARY KEY (idUtilisateur, idEconome),
    FOREIGN KEY (idUtilisateur) REFERENCES Utilisateur (idUtilisateur) ON DELETE CASCADE
);

CREATE TABLE Eleve
(
    matriculeEleve        TEXT NOT NULL UNIQUE ,
    idParent              TEXT NOT NULL,
    nom                   TEXT,
    prenom                TEXT,
    dateNaissance         TEXT,
    lieuNaissance         TEXT,
    sexe                  TEXT CHECK(sexe IN ('F', 'M')),
    nationnalite          TEXT,
    photo                 TEXT,
    aTerminerPension      INTEGER DEFAULT 0,
    is_synced             INTEGER DEFAULT 0,
    date_creation         TEXT DEFAULT CURRENT_TIMESTAMP,
    derniere_modification TEXT DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (matriculeEleve),
    FOREIGN KEY (idParent) REFERENCES Parent (idParent)
);

CREATE TABLE TarisScolaire
(
    idTarifScolaire       TEXT NOT NULL,
    idClasse              TEXT NOT NULL,
    libelle               TEXT,
    montantApee           REAL,
    fraistenueScolaire    REAL,
    fraistenueSport       REAL,
    fraisInscription      REAL,
    PRIMARY KEY (idTarifScolaire),
    FOREIGN KEY (idClasse) REFERENCES Classe (idClasse)
);

CREATE TABLE Enseignant
(
    idEnseignant          TEXT NOT NULL,
    idClasse              TEXT NOT NULL,
    nom                   TEXT,
    prenom                TEXT,
    contactEnseignant     INTEGER,
    qualification         TEXT,
    grade                 TEXT,
    is_synced             INTEGER DEFAULT 0,
    derniere_modification TEXT DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (idEnseignant),
    FOREIGN KEY (idClasse) REFERENCES Classe (idClasse)
);

CREATE TABLE Matiere
(
    idMatiere             TEXT NOT NULL,
    idClasse              TEXT NOT NULL,
    libelle               TEXT,
    coefficient           REAL,
    volumeHoraire         INTEGER,
    categorie             TEXT,
    PRIMARY KEY (idMatiere),
    FOREIGN KEY (idClasse) REFERENCES Classe (idClasse) -- Virgule en trop supprimée ici
);

CREATE TABLE Inscription
(
    idInscription         TEXT NOT NULL,
    idClasse              TEXT NOT NULL,
    matriculeEleve        TEXT,
    montantPayer          REAL,
    estReinscript         INTEGER DEFAULT 0,
    dateInscription       TEXT DEFAULT CURRENT_TIMESTAMP,
    is_synced             INTEGER DEFAULT 0,
    date_creation         TEXT DEFAULT CURRENT_TIMESTAMP,
    derniere_modification TEXT DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (idInscription),
    FOREIGN KEY (idClasse) REFERENCES Classe (idClasse),
    FOREIGN KEY (matriculeEleve) REFERENCES Eleve (matriculeEleve)
);

CREATE TABLE DocumentScolaire
(
    matriculeEleve        TEXT NOT NULL,
    idDocuments           TEXT NOT NULL,
    type                  TEXT CHECK(type IN ('fiche', 'certif')),
    dateGeneration        TEXT,
    generePar             TEXT,
    formatFichier         TEXT,
    is_synced             INTEGER DEFAULT 0,
    date_creation         TEXT DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (matriculeEleve, idDocuments),
    FOREIGN KEY (matriculeEleve) REFERENCES Eleve (matriculeEleve) ON DELETE CASCADE
);

/*==============================================================*/
/* [NIVEAU 3] Documents spécifiques et hiérarchie scolaire      */
/*==============================================================*/

CREATE TABLE certificat_scolarite
(
    matriculeEleve        TEXT NOT NULL,
    idDocuments           TEXT NOT NULL,
    idcertificat          TEXT NOT NULL,
    PRIMARY KEY (matriculeEleve, idDocuments, idcertificat),
    FOREIGN KEY (matriculeEleve, idDocuments) REFERENCES DocumentScolaire (matriculeEleve, idDocuments) ON DELETE CASCADE
);

CREATE TABLE fiche_renseignement
(
    matriculeEleve        TEXT NOT NULL,
    idDocuments           TEXT NOT NULL,
    idficheRenseignement  TEXT NOT NULL,
    PRIMARY KEY (matriculeEleve, idDocuments, idficheRenseignement),
    FOREIGN KEY (matriculeEleve, idDocuments) REFERENCES DocumentScolaire (matriculeEleve, idDocuments) ON DELETE CASCADE
);

CREATE TABLE recue_paiement
(
    matriculeEleve        TEXT NOT NULL,
    idDocuments           TEXT NOT NULL,
    idRecu                TEXT NOT NULL,
    PRIMARY KEY (matriculeEleve, idDocuments, idRecu),
    FOREIGN KEY (matriculeEleve, idDocuments) REFERENCES DocumentScolaire (matriculeEleve, idDocuments) ON DELETE CASCADE
);

CREATE TABLE Notes
(
    idNote                TEXT NOT NULL,
    valeur                REAL,
    noteMax               INTEGER,
    observation           TEXT,
    dateSaisie            TEXT DEFAULT CURRENT_TIMESTAMP,
    saisirPar             TEXT,
    idAnnescolaire        TEXT NOT NULL,
    idTrimestre           TEXT NOT NULL,
    idMatiere             TEXT NOT NULL,
    idSequence            TEXT NOT NULL,
    matriculeEleve        TEXT NOT NULL,
    is_synced             INTEGER DEFAULT 0,
    date_creation         TEXT DEFAULT CURRENT_TIMESTAMP,
    derniere_modification TEXT DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (idNote),
    FOREIGN KEY (matriculeEleve) REFERENCES Eleve (matriculeEleve),
    FOREIGN KEY (idMatiere) REFERENCES Matiere (idMatiere),
    FOREIGN KEY (idAnnescolaire, idTrimestre, idSequence) REFERENCES Sequence (idAnnescolaire, idTrimestre, idSequence)
);

-- Note : Placé APRÈS Notes et DocumentScolaire pour que les clés étrangères existent !
CREATE TABLE Bulletin
(
    matriculeEleve        TEXT NOT NULL,
    idDocuments           TEXT NOT NULL,
    id_bulletin           TEXT NOT NULL,
    idNote                TEXT NOT NULL, -- Corrigé : idNotes est devenu idNote (cohérent avec la table Notes)
    PRIMARY KEY (matriculeEleve, idDocuments, id_bulletin),
    FOREIGN KEY (idNote) REFERENCES Notes (idNote),
    FOREIGN KEY (matriculeEleve, idDocuments) REFERENCES DocumentScolaire (matriculeEleve, idDocuments) ON DELETE CASCADE
);

/*==============================================================*/
/* [NIVEAU 4] Cycles d'évaluations et Paiements Finaux          */
/*==============================================================*/

CREATE TABLE Trimestre
(
    idAnnescolaire        TEXT NOT NULL,
    idTrimestre           TEXT NOT NULL,
    matriculeEleve        TEXT NOT NULL,
    idDocuments           TEXT NOT NULL,
    id_bulletin           TEXT NOT NULL,
    libelle               TEXT,
    dateDebut             TEXT,
    dateFin               TEXT,
    estClos               INTEGER DEFAULT 0,
    PRIMARY KEY (idAnnescolaire, idTrimestre),
    FOREIGN KEY (idAnnescolaire) REFERENCES AnneeScolaire (idAnnescolaire),
    FOREIGN KEY (matriculeEleve, idDocuments, id_bulletin) REFERENCES Bulletin (matriculeEleve, idDocuments, id_bulletin)
);

CREATE TABLE Sequence
(
    idAnnescolaire        TEXT NOT NULL,
    idTrimestre           TEXT NOT NULL,
    idSequence            TEXT NOT NULL,
    libelle               TEXT,
    dateComposition       TEXT,
    estSaisie             INTEGER DEFAULT 0,
    PRIMARY KEY (idAnnescolaire, idTrimestre, idSequence),
    FOREIGN KEY (idAnnescolaire, idTrimestre) REFERENCES Trimestre (idAnnescolaire, idTrimestre)
);

CREATE TABLE Paiement
(
    reference             TEXT NOT NULL,
    montant               REAL,
    datePaiement          TEXT DEFAULT CURRENT_TIMESTAMP,
    modePaiement          TEXT,
    encaisserPar          TEXT,
    idPaiement            TEXT NOT NULL,
    idTarifScolaire       TEXT NOT NULL,
    matriculeEleve        TEXT NOT NULL,
    idDocuments           TEXT NOT NULL,
    idRecu                TEXT NOT NULL,
    idUtilisateur         TEXT NOT NULL,
    idEconome             TEXT NOT NULL,
    is_synced             INTEGER DEFAULT 0,
    date_creation         TEXT DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (idPaiement),
    FOREIGN KEY (matriculeEleve, idDocuments, idRecu) REFERENCES recue_paiement (matriculeEleve, idDocuments, idRecu),
    FOREIGN KEY (idTarifScolaire) REFERENCES TarisScolaire (idTarifScolaire),
    FOREIGN KEY (idUtilisateur, idEconome) REFERENCES Econome (idUtilisateur, idEconome)
);

/*==============================================================*/
/* [NIVEAU 5] Tables d'associations de jointures                */
/*==============================================================*/

CREATE TABLE association6
(
    idInscription         TEXT NOT NULL,
    idClasse              TEXT NOT NULL,
    PRIMARY KEY (idInscription, idClasse),
    FOREIGN KEY (idClasse) REFERENCES Classe (idClasse),
    FOREIGN KEY (idInscription) REFERENCES Inscription (idInscription) ON DELETE CASCADE
);

CREATE TABLE association7
(
    idInscription         TEXT NOT NULL,
    idAnnescolaire        TEXT NOT NULL,
    PRIMARY KEY (idInscription, idAnnescolaire),
    FOREIGN KEY (idAnnescolaire) REFERENCES AnneeScolaire (idAnnescolaire),
    FOREIGN KEY (idInscription) REFERENCES Inscription (idInscription) ON DELETE CASCADE
);

CREATE TABLE paiementTranche
(
    idPaiement            TEXT NOT NULL,
    idTranche             TEXT NOT NULL,
    PRIMARY KEY (idPaiement, idTranche),
    FOREIGN KEY (idPaiement) REFERENCES Paiement (idPaiement) ON DELETE CASCADE,
    FOREIGN KEY (idTranche) REFERENCES Tranche (idTranche)
);