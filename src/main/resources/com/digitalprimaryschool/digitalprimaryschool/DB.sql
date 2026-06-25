-- ==============================================================
-- [NIVEAU 0] CONFIGURATION ET ÉTABLISSEMENT
-- ==============================================================

CREATE TABLE IF NOT EXISTS Ecole
(
    idEcole               INT AUTO_INCREMENT NOT NULL,
    nomEcole              VARCHAR(150) NOT NULL,
    numeroArreteOuverture VARCHAR(100), -- Très important au Cameroun (Légalisation)
    nomPromoteur          VARCHAR(100),
    inspectionRef         VARCHAR(100), -- Ex: Inspection d'Arrondissement de Yaoundé IV
    delegationDept        VARCHAR(100), -- Ex: Délégation Départementale du Mfoundi
    region                VARCHAR(50),  -- Ex: Centre, Littoral, Adamaoua...
    sousSysteme           VARCHAR(30),  -- Francophone, Anglophone ou Bilingue
    contactEcole          VARCHAR(30),
    emailEcole            VARCHAR(100),
    adressePhysique       VARCHAR(150),
    devise                VARCHAR(150), -- Utile pour le bas des bulletins
    logoPath              VARCHAR(255), -- Chemin local de l'image pour les impressions
    PRIMARY KEY (idEcole)
    ) ENGINE=InnoDB;

-- ==============================================================
-- [NIVEAU 1] TABLES DE BASE DÉPENDANTES DE L'ÉCOLE
-- ==============================================================

CREATE TABLE IF NOT EXISTS AnneeScolaire
(
    idAnnescolaire        VARCHAR(50) NOT NULL,
    idEcole               INT NOT NULL,
    libelle               VARCHAR(100),
    dateDebut             DATE,
    dateFin               DATE,
    estActive             TINYINT(1) DEFAULT 0,
    PRIMARY KEY (idAnnescolaire),
    FOREIGN KEY (idEcole) REFERENCES Ecole (idEcole) ON DELETE CASCADE
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS TarisScolaire
(
    idTarifScolaire       VARCHAR(50) NOT NULL,
    idEcole               INT NOT NULL,
    niveauClasse          VARCHAR(100),
    libelle               VARCHAR(100),
    pension               DOUBLE,
    fraistenueScolaire    DOUBLE,
    fraistenueSport       DOUBLE,
    fraisInscription      DOUBLE,
    PRIMARY KEY (idTarifScolaire),
    FOREIGN KEY (idEcole) REFERENCES Ecole (idEcole) ON DELETE CASCADE
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Tranche
(
    idTranche             VARCHAR(50) NOT NULL,
    idTarifScolaire       VARCHAR(50) NOT NULL,
    libelleTranche        VARCHAR(100),
    montantTranche        DOUBLE,
    dateEcheance          DATE,
    estPaye               TINYINT(1) DEFAULT 0,
    PRIMARY KEY (idTranche),
    FOREIGN KEY (idTarifScolaire) REFERENCES TarisScolaire (idTarifScolaire) ON DELETE CASCADE
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Utilisateur
(
    idUtilisateur         VARCHAR(50) NOT NULL,
    idEcole               INT NOT NULL,
    username              VARCHAR(100),
    motDePasseUtilisateur VARCHAR(255),
    role                  VARCHAR(50),
    is_synced             TINYINT(1) DEFAULT 0,
    derniere_modification DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (idUtilisateur),
    FOREIGN KEY (idEcole) REFERENCES Ecole (idEcole) ON DELETE CASCADE
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Parent
(
    idParent              VARCHAR(50) NOT NULL,
    prenom                VARCHAR(100),
    contactParent         INTEGER,
    emailParent           VARCHAR(150),
    profession            VARCHAR(100),
    adresse               VARCHAR(150),
    is_synced             TINYINT(1) DEFAULT 0,
    date_creation         DATETIME DEFAULT CURRENT_TIMESTAMP,
    derniere_modification DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (idParent)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Classe
(
    idClasse              VARCHAR(50) NOT NULL,
    idEcole               INT NOT NULL,
    nom                   VARCHAR(100),
    niveau                VARCHAR(50),
    capaciteMax           INTEGER,
    section               VARCHAR(50),
    PRIMARY KEY (idClasse),
    FOREIGN KEY (idEcole) REFERENCES Ecole (idEcole) ON DELETE CASCADE
    ) ENGINE=InnoDB;

-- ==============================================================
-- [NIVEAU 2] TABLES ASSOCIÉES ET DOCUMENTS
-- ==============================================================

CREATE TABLE IF NOT EXISTS Econome
(
    idUtilisateur         VARCHAR(50) NOT NULL,
    idEconome             VARCHAR(50) NOT NULL,
    PRIMARY KEY (idUtilisateur, idEconome),
    FOREIGN KEY (idUtilisateur) REFERENCES Utilisateur (idUtilisateur) ON DELETE CASCADE
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Eleve
(
    matriculeEleve        VARCHAR(50) NOT NULL UNIQUE,
    idParent              VARCHAR(50) NOT NULL,
    nom                   VARCHAR(100),
    prenom                VARCHAR(100),
    dateNaissance         DATE,
    lieuNaissance         VARCHAR(100),
    sexe                  VARCHAR(10),
    nationalite          VARCHAR(50),
    photo                 VARCHAR(255),
    aTerminerPension      TINYINT(1) DEFAULT 0,
    regionOrigine         VARCHAR(100),
    antecedentsMedicaux   TEXT,
    is_synced             TINYINT(1) DEFAULT 0,
    date_creation         DATETIME DEFAULT CURRENT_TIMESTAMP,
    derniere_modification DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (matriculeEleve),
    FOREIGN KEY (idParent) REFERENCES Parent (idParent),
    CONSTRAINT chk_sexe CHECK (sexe IN ('FEMININ', 'MASCULIN'))
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Enseignant
(
    idEnseignant          VARCHAR(50) NOT NULL,
    idClasse              VARCHAR(50) NOT NULL,
    nom                   VARCHAR(100),
    prenom                VARCHAR(100),
    contactEnseignant     INTEGER,
    qualification         VARCHAR(100),
    grade                 VARCHAR(50),
    is_synced             TINYINT(1) DEFAULT 0,
    derniere_modification DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (idEnseignant),
    FOREIGN KEY (idClasse) REFERENCES Classe (idClasse)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Matiere
(
    idMatiere             VARCHAR(50) NOT NULL,
    idClasse              VARCHAR(50) NOT NULL,
    libelle               VARCHAR(100),
    coefficient           DOUBLE,
    volumeHoraire         INTEGER,
    categorie             VARCHAR(100),
    PRIMARY KEY (idMatiere),
    FOREIGN KEY (idClasse) REFERENCES Classe (idClasse)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Inscription
(
    idInscription         VARCHAR(50) NOT NULL,
    idAnnescolaire        VARCHAR(50) NOT NULL,
    idClasse              VARCHAR(50) NOT NULL,
    matriculeEleve        VARCHAR(50),
    montantPayer          DOUBLE,
    estReinscript         TINYINT(1) DEFAULT 0,
    dateInscription       DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_synced             TINYINT(1) DEFAULT 0,
    date_creation         DATETIME DEFAULT CURRENT_TIMESTAMP,
    derniere_modification DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (idInscription),
    FOREIGN KEY (idClasse) REFERENCES Classe (idClasse),
    FOREIGN KEY (idAnnescolaire) REFERENCES AnneeScolaire (idAnnescolaire),
    FOREIGN KEY (matriculeEleve) REFERENCES Eleve (matriculeEleve)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS DocumentScolaire
(
    matriculeEleve        VARCHAR(50) NOT NULL,
    idDocuments           VARCHAR(50) NOT NULL,
    type                  VARCHAR(10),
    dateGeneration        DATETIME DEFAULT CURRENT_TIMESTAMP,
    generePar             VARCHAR(100),
    formatFichier         VARCHAR(10),
    is_synced             TINYINT(1) DEFAULT 0,
    date_creation         DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (matriculeEleve, idDocuments),
    FOREIGN KEY (matriculeEleve) REFERENCES Eleve (matriculeEleve) ON DELETE CASCADE,
    CONSTRAINT chk_type CHECK (type IN ('fiche', 'certif'))
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS certificat_scolarite
(
    matriculeEleve        VARCHAR(50) NOT NULL,
    idDocuments           VARCHAR(50) NOT NULL,
    idcertificat          VARCHAR(50) NOT NULL,
    PRIMARY KEY (matriculeEleve, idDocuments, idcertificat),
    FOREIGN KEY (matriculeEleve, idDocuments) REFERENCES DocumentScolaire (matriculeEleve, idDocuments) ON DELETE CASCADE
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS fiche_renseignement
(
    matriculeEleve        VARCHAR(50) NOT NULL,
    idDocuments           VARCHAR(50) NOT NULL,
    idficheRenseignement  VARCHAR(50) NOT NULL,
    PRIMARY KEY (matriculeEleve, idDocuments, idficheRenseignement),
    FOREIGN KEY (matriculeEleve, idDocuments) REFERENCES DocumentScolaire (matriculeEleve, idDocuments) ON DELETE CASCADE
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS recue_paiement
(
    matriculeEleve        VARCHAR(50) NOT NULL,
    idDocuments           VARCHAR(50) NOT NULL,
    idRecu                VARCHAR(50) NOT NULL,
    PRIMARY KEY (matriculeEleve, idDocuments, idRecu),
    FOREIGN KEY (matriculeEleve, idDocuments) REFERENCES DocumentScolaire (matriculeEleve, idDocuments) ON DELETE CASCADE
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Trimestre
(
    idAnnescolaire        VARCHAR(50) NOT NULL,
    idTrimestre           VARCHAR(50) NOT NULL,
    libelle               VARCHAR(100),
    dateDebut             DATE,
    dateFin               DATE,
    estClos               TINYINT(1) DEFAULT 0,
    PRIMARY KEY (idAnnescolaire, idTrimestre),
    FOREIGN KEY (idAnnescolaire) REFERENCES AnneeScolaire (idAnnescolaire)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Sequence
(
    idAnnescolaire        VARCHAR(50) NOT NULL,
    idTrimestre           VARCHAR(50) NOT NULL,
    idSequence            VARCHAR(50) NOT NULL,
    libelle               VARCHAR(100),
    dateComposition       DATE,
    estSaisie             TINYINT(1) DEFAULT 0,
    PRIMARY KEY (idAnnescolaire, idTrimestre, idSequence),
    FOREIGN KEY (idAnnescolaire, idTrimestre) REFERENCES Trimestre (idAnnescolaire, idTrimestre)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Notes
(
    idNote                VARCHAR(50) NOT NULL,
    valeur                DOUBLE,
    noteMax               INTEGER,
    observation           VARCHAR(255),
    dateSaisie            DATETIME DEFAULT CURRENT_TIMESTAMP,
    saisirPar             VARCHAR(100),
    idAnnescolaire        VARCHAR(50) NOT NULL,
    idTrimestre           VARCHAR(50) NOT NULL,
    idMatiere             VARCHAR(50) NOT NULL,
    idSequence            VARCHAR(50) NOT NULL,
    matriculeEleve        VARCHAR(50) NOT NULL,
    is_synced             TINYINT(1) DEFAULT 0,
    date_creation         DATETIME DEFAULT CURRENT_TIMESTAMP,
    derniere_modification DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (idNote),
    FOREIGN KEY (matriculeEleve) REFERENCES Eleve (matriculeEleve),
    FOREIGN KEY (idMatiere) REFERENCES Matiere (idMatiere),
    FOREIGN KEY (idAnnescolaire, idTrimestre, idSequence) REFERENCES Sequence (idAnnescolaire, idTrimestre, idSequence)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Bulletin
(
    idBulletin            VARCHAR(50) NOT NULL,
    matriculeEleve        VARCHAR(50) NOT NULL,
    idDocuments           VARCHAR(50) NOT NULL,
    idAnnescolaire        VARCHAR(50) NOT NULL,
    idTrimestre           VARCHAR(50) NOT NULL,
    dateGeneration        DATETIME DEFAULT CURRENT_TIMESTAMP,
    moyenneGenerale       DOUBLE,
    appreciation          VARCHAR(255),
    rang                  INTEGER,
    PRIMARY KEY (idBulletin),
    FOREIGN KEY (matriculeEleve, idDocuments) REFERENCES DocumentScolaire (matriculeEleve, idDocuments) ON DELETE CASCADE,
    FOREIGN KEY (idAnnescolaire, idTrimestre) REFERENCES Trimestre (idAnnescolaire, idTrimestre)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Bulletin_Notes
(
    idBulletin            VARCHAR(50) NOT NULL,
    idNote                VARCHAR(50) NOT NULL,
    PRIMARY KEY (idBulletin, idNote),
    FOREIGN KEY (idBulletin) REFERENCES Bulletin (idBulletin) ON DELETE CASCADE,
    FOREIGN KEY (idNote) REFERENCES Notes (idNote) ON DELETE CASCADE
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Trimestre_Bulletin
(
    idAnnescolaire        VARCHAR(50) NOT NULL,
    idTrimestre           VARCHAR(50) NOT NULL,
    idBulletin            VARCHAR(50) NOT NULL,
    PRIMARY KEY (idAnnescolaire, idTrimestre, idBulletin),
    FOREIGN KEY (idAnnescolaire, idTrimestre) REFERENCES Trimestre (idAnnescolaire, idTrimestre) ON DELETE CASCADE,
    FOREIGN KEY (idBulletin) REFERENCES Bulletin (idBulletin) ON DELETE CASCADE
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS Paiement
(
    reference             VARCHAR(100) NOT NULL,
    montant               DOUBLE,
    datePaiement          DATETIME DEFAULT CURRENT_TIMESTAMP,
    modePaiement          VARCHAR(50),
    encaisserPar          VARCHAR(100),
    idPaiement            VARCHAR(50) NOT NULL,
    idTarifScolaire       VARCHAR(50) NOT NULL,
    matriculeEleve        VARCHAR(50) NOT NULL,
    idDocuments           VARCHAR(50) NOT NULL,
    idRecu                VARCHAR(50) NOT NULL,
    idUtilisateur         VARCHAR(50) NOT NULL,
    idEconome             VARCHAR(50) NOT NULL,
    is_synced             TINYINT(1) DEFAULT 0,
    date_creation         DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (idPaiement),
    FOREIGN KEY (matriculeEleve, idDocuments, idRecu) REFERENCES recue_paiement (matriculeEleve, idDocuments, idRecu),
    FOREIGN KEY (idTarifScolaire) REFERENCES TarisScolaire (idTarifScolaire),
    FOREIGN KEY (idUtilisateur, idEconome) REFERENCES Econome (idUtilisateur, idEconome)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS paiementTranche
(
    idPaiement            VARCHAR(50) NOT NULL,
    idTranche             VARCHAR(50) NOT NULL,
    PRIMARY KEY (idPaiement, idTranche),
    FOREIGN KEY (idPaiement) REFERENCES Paiement (idPaiement) ON DELETE CASCADE,
    FOREIGN KEY (idTranche) REFERENCES Tranche (idTranche)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS association6
(
    idInscription         VARCHAR(50) NOT NULL,
    idClasse              VARCHAR(50) NOT NULL,
    PRIMARY KEY (idInscription, idClasse),
    FOREIGN KEY (idClasse) REFERENCES Classe (idClasse),
    FOREIGN KEY (idInscription) REFERENCES Inscription (idInscription) ON DELETE CASCADE
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS association7
(
    idInscription         VARCHAR(50) NOT NULL,
    idAnnescolaire        VARCHAR(50) NOT NULL,
    PRIMARY KEY (idInscription, idAnnescolaire),
    FOREIGN KEY (idAnnescolaire) REFERENCES AnneeScolaire (idAnnescolaire),
    FOREIGN KEY (idInscription) REFERENCES Inscription (idInscription) ON DELETE CASCADE
    ) ENGINE=InnoDB;
