PRAGMA foreign_keys = ON;


CREATE TABLE IF NOT EXISTS AnneeScolaire
(
    idAnnescolaire        TEXT NOT NULL,
    libelle               TEXT,
    dateDebut             TEXT,
    dateFin               TEXT,
    estActive             INTEGER DEFAULT 0,
    PRIMARY KEY (idAnnescolaire)
    );

CREATE TABLE IF NOT EXISTS Tranche
(
    idTranche             TEXT NOT NULL,
    idTarifScolaire       TEXT NOT NULL, -- Lien direct avec le tarif du niveau
    libelleTranche        TEXT,          -- Ex: "1ère Tranche (60%)"
    montantTranche        REAL,
    dateEcheance          TEXT,          -- Date limite de paiement
    estPaye               INTEGER DEFAULT 0,
    PRIMARY KEY (idTranche),
    FOREIGN KEY (idTarifScolaire) REFERENCES TarisScolaire (idTarifScolaire) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS Utilisateur
(
    idUtilisateur         TEXT NOT NULL,
    username              TEXT,
    motDePasseUtilisateur TEXT,
    role                  TEXT,
    is_synced             INTEGER DEFAULT 0,
    derniere_modification TEXT DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (idUtilisateur)
    );

CREATE TABLE IF NOT EXISTS Parent
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

CREATE TABLE IF NOT EXISTS Classe
(
    idClasse              TEXT NOT NULL,
    nom                   TEXT,
    niveau                TEXT,
    capaciteMax           INTEGER,
    section               TEXT,
    PRIMARY KEY (idClasse)
    );

/*==============================================================*/
/* [NIVEAU 2] Tables dépendantes du Niveau 1                     */
/*==============================================================*/

CREATE TABLE IF NOT EXISTS Econome
(
    idUtilisateur         TEXT NOT NULL,
    idEconome             TEXT NOT NULL,
    PRIMARY KEY (idUtilisateur, idEconome),
    FOREIGN KEY (idUtilisateur) REFERENCES Utilisateur (idUtilisateur) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS Eleve
(
    matriculeEleve        TEXT NOT NULL UNIQUE,
    idParent              TEXT NOT NULL,
    nom                   TEXT,
    prenom                TEXT,
    dateNaissance         TEXT,
    lieuNaissance         TEXT,
    sexe                  TEXT CHECK(sexe IN ('FEMININ', 'MASCULIN')),
    nationnalite          TEXT,
    photo                 TEXT,
    aTerminerPension      INTEGER DEFAULT 0,
    regionOrigine         TEXT,
    antecedentsMedicaux   TEXT,
    is_synced             INTEGER DEFAULT 0,
    date_creation         TEXT DEFAULT CURRENT_TIMESTAMP,
    derniere_modification TEXT DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (matriculeEleve),
    FOREIGN KEY (idParent) REFERENCES Parent (idParent)
    );

CREATE TABLE IF NOT EXISTS TarisScolaire
(
    idTarifScolaire       TEXT NOT NULL,
    niveauClasse          TEXT,
    libelle               TEXT,
    pension               REAL,
    fraistenueScolaire    REAL,
    fraistenueSport       REAL,
    fraisInscription      REAL,
    PRIMARY KEY (idTarifScolaire)
    );

CREATE TABLE IF NOT EXISTS Enseignant
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

CREATE TABLE IF NOT EXISTS Matiere
(
    idMatiere             TEXT NOT NULL,
    idClasse              TEXT NOT NULL,
    libelle               TEXT,
    coefficient           REAL,
    volumeHoraire         INTEGER,
    categorie             TEXT,
    PRIMARY KEY (idMatiere),
    FOREIGN KEY (idClasse) REFERENCES Classe (idClasse)
    );

CREATE TABLE IF NOT EXISTS Inscription
(
    idInscription         TEXT NOT NULL,
    idAnnescolaire        TEXT NOT NULL,
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
    FOREIGN KEY (idAnnescolaire) REFERENCES AnneeScolaire (idAnnescolaire),
    FOREIGN KEY (matriculeEleve) REFERENCES Eleve (matriculeEleve)
    );

CREATE TABLE IF NOT EXISTS DocumentScolaire
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

CREATE TABLE IF NOT EXISTS certificat_scolarite
(
    matriculeEleve        TEXT NOT NULL,
    idDocuments           TEXT NOT NULL,
    idcertificat          TEXT NOT NULL,
    PRIMARY KEY (matriculeEleve, idDocuments, idcertificat),
    FOREIGN KEY (matriculeEleve, idDocuments) REFERENCES DocumentScolaire (matriculeEleve, idDocuments) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS fiche_renseignement
(
    matriculeEleve        TEXT NOT NULL,
    idDocuments           TEXT NOT NULL,
    idficheRenseignement  TEXT NOT NULL,
    PRIMARY KEY (matriculeEleve, idDocuments, idficheRenseignement),
    FOREIGN KEY (matriculeEleve, idDocuments) REFERENCES DocumentScolaire (matriculeEleve, idDocuments) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS recue_paiement
(
    matriculeEleve        TEXT NOT NULL,
    idDocuments           TEXT NOT NULL,
    idRecu                TEXT NOT NULL,
    PRIMARY KEY (matriculeEleve, idDocuments, idRecu),
    FOREIGN KEY (matriculeEleve, idDocuments) REFERENCES DocumentScolaire (matriculeEleve, idDocuments) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS Trimestre
(
    idAnnescolaire        TEXT NOT NULL,
    idTrimestre           TEXT NOT NULL,
    libelle               TEXT,
    dateDebut             TEXT,
    dateFin               TEXT,
    estClos               INTEGER DEFAULT 0,
    PRIMARY KEY (idAnnescolaire, idTrimestre),
    FOREIGN KEY (idAnnescolaire) REFERENCES AnneeScolaire (idAnnescolaire)
    );

CREATE TABLE IF NOT EXISTS Sequence
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

CREATE TABLE IF NOT EXISTS Notes
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

CREATE TABLE IF NOT EXISTS Bulletin
(
    idBulletin            TEXT NOT NULL,
    matriculeEleve        TEXT NOT NULL,
    idDocuments           TEXT NOT NULL,
    idAnnescolaire        TEXT NOT NULL,
    idTrimestre           TEXT NOT NULL,
    dateGeneration        TEXT DEFAULT CURRENT_TIMESTAMP,
    moyenneGenerale       REAL,
    appreciation          TEXT,
    rang                  INTEGER,
    PRIMARY KEY (idBulletin),
    FOREIGN KEY (matriculeEleve, idDocuments) REFERENCES DocumentScolaire (matriculeEleve, idDocuments) ON DELETE CASCADE,
    FOREIGN KEY (idAnnescolaire, idTrimestre) REFERENCES Trimestre (idAnnescolaire, idTrimestre)
    );

CREATE TABLE IF NOT EXISTS Bulletin_Notes
(
    idBulletin            TEXT NOT NULL,
    idNote                TEXT NOT NULL,
    PRIMARY KEY (idBulletin, idNote),
    FOREIGN KEY (idBulletin) REFERENCES Bulletin (idBulletin) ON DELETE CASCADE,
    FOREIGN KEY (idNote) REFERENCES Notes (idNote) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS Trimestre_Bulletin
(
    idAnnescolaire        TEXT NOT NULL,
    idTrimestre           TEXT NOT NULL,
    idBulletin            TEXT NOT NULL,
    PRIMARY KEY (idAnnescolaire, idTrimestre, idBulletin),
    FOREIGN KEY (idAnnescolaire, idTrimestre) REFERENCES Trimestre (idAnnescolaire, idTrimestre) ON DELETE CASCADE,
    FOREIGN KEY (idBulletin) REFERENCES Bulletin (idBulletin) ON DELETE CASCADE
    );

/*==============================================================*/
/* [NIVEAU 4] Paiements                                         */
/*==============================================================*/

CREATE TABLE IF NOT EXISTS Paiement
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

CREATE TABLE IF NOT EXISTS paiementTranche
(
    idPaiement            TEXT NOT NULL,
    idTranche             TEXT NOT NULL,
    PRIMARY KEY (idPaiement, idTranche),
    FOREIGN KEY (idPaiement) REFERENCES Paiement (idPaiement) ON DELETE CASCADE,
    FOREIGN KEY (idTranche) REFERENCES Tranche (idTranche)
    );

/*==============================================================*/
/* [NIVEAU 5] Tables d'associations                             */
/*==============================================================*/

CREATE TABLE IF NOT EXISTS association6
(
    idInscription         TEXT NOT NULL,
    idClasse              TEXT NOT NULL,
    PRIMARY KEY (idInscription, idClasse),
    FOREIGN KEY (idClasse) REFERENCES Classe (idClasse),
    FOREIGN KEY (idInscription) REFERENCES Inscription (idInscription) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS association7
(
    idInscription         TEXT NOT NULL,
    idAnnescolaire        TEXT NOT NULL,
    PRIMARY KEY (idInscription, idAnnescolaire),
    FOREIGN KEY (idAnnescolaire) REFERENCES AnneeScolaire (idAnnescolaire),
    FOREIGN KEY (idInscription) REFERENCES Inscription (idInscription) ON DELETE CASCADE
    );

/*==============================================================*/
/* [INDEX] Optimisation des performances                        */
/*==============================================================*/

CREATE INDEX IF NOT EXISTS idx_eleve_parent ON Eleve(idParent);
CREATE INDEX IF NOT EXISTS idx_inscription_eleve_annee ON Inscription(matriculeEleve, idAnnescolaire);
CREATE INDEX IF NOT EXISTS idx_notes_eleve_trimestre ON Notes(matriculeEleve, idAnnescolaire, idTrimestre);
CREATE INDEX IF NOT EXISTS idx_paiement_eleve ON Paiement(matriculeEleve);
CREATE INDEX IF NOT EXISTS idx_bulletin_eleve_trimestre ON Bulletin(matriculeEleve, idAnnescolaire, idTrimestre);
CREATE INDEX IF NOT EXISTS idx_notes_valeur ON Notes(valeur DESC);
CREATE INDEX IF NOT EXISTS idx_paiement_date ON Paiement(datePaiement DESC);
CREATE INDEX IF NOT EXISTS idx_inscription_date ON Inscription(dateInscription DESC);