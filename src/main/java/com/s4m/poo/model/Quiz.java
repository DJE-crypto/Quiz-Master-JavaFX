package com.s4m.poo.model;

import java.time.LocalDate;
import javafx.beans.property.*;

public class Quiz {

    private IntegerProperty idQuiz = new SimpleIntegerProperty();
    private StringProperty titre = new SimpleStringProperty();
    private ObjectProperty<LocalDate> dateCreation = new SimpleObjectProperty<>();
    private IntegerProperty utilisateurId = new SimpleIntegerProperty();
    private ObjectProperty<Theme> theme = new SimpleObjectProperty<>();

    public Quiz() {
    }

    // Constructeur complet
    public Quiz(int idQuiz, String titre, LocalDate dateCreation, int utilisateurId, Theme theme) {
        this.idQuiz.set(idQuiz);
        this.titre.set(titre);
        this.dateCreation.set(dateCreation);
        this.utilisateurId.set(utilisateurId);
        this.theme.set(theme);
    }

    // --- Property Getters (pour JavaFX binding) ---
    public IntegerProperty idQuizProperty() { return idQuiz; }
    public StringProperty titreProperty() { return titre; }
    public ObjectProperty<LocalDate> dateCreationProperty() { return dateCreation; }
    public IntegerProperty utilisateurIdProperty() { return utilisateurId; }
    public ObjectProperty<Theme> themeProperty() { return theme; }

    // --- Getters Standards ---
    public int getIdQuiz() { return idQuiz.get(); }
    public String getTitre() { return titre.get(); }
    public LocalDate getDateCreation() { return dateCreation.get(); }
    public int getUtilisateurId() { return utilisateurId.get(); }
    public Theme getTheme() { return theme.get(); }

    // --- Setters Standards ---
    public void setIdQuiz(int idQuiz) { this.idQuiz.set(idQuiz); }
    public void setTitre(String titre) { this.titre.set(titre); }
    public void setDateCreation(LocalDate dateCreation) { this.dateCreation.set(dateCreation); }
    public void setUtilisateurId(int utilisateurId) { this.utilisateurId.set(utilisateurId); }
    public void setTheme(Theme theme) { this.theme.set(theme); }

    // ESSENTIEL pour l'affichage dans les ComboBox de JavaFX
    @Override
    public String toString() {
        return getTitre();
    }
}