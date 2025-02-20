<template>
  <div class="form-container">
    <form @submit.prevent="submitForm">
      <!-- Sélection Personne -->
      <div class="form-group">
        <label for="personne">Sélectionner une personne :</label>
        <select v-model="selectedPersonne" required>
          <option v-for="personne in personnes" :key="personne.matricule" :value="personne.matricule">
            {{ personne.nom }} {{ personne.prenom }}
          </option>
        </select>
      </div>

      <!-- Sélection Projet -->
      <div class="form-group">
        <label for="projet">Sélectionner un projet :</label>
        <select v-model="selectedProjet" required>
          <option v-for="projet in projets" :key="projet.id" :value="projet.id">
            {{ projet.nom }}
          </option>
        </select>
      </div>

      <!-- Champ Rôle -->
      <div class="form-group">
        <label for="role">Rôle :</label>
        <input type="text" v-model="role" required />
      </div>

      <!-- Barre de pourcentage avec valeur en temps réel -->
      <div class="form-group">
        <label for="pourcentage">Pourcentage de participation :</label>
        <input
          type="range"
          v-model="pourcentage"
          min="0"
          max="100"
          step="1"
          required
          @input="updatePourcentage"
          :style="{ background: getRangeBackground() }"
        />
        <!-- Affichage de la valeur en temps réel -->
        <div class="range-value">
          <span>{{ pourcentage }}%</span>
        </div>
      </div>

      <!-- Conteneur pour le bouton -->
      <div class="button-container">
        <button type="submit">Enregistrer</button>
      </div>
    </form>

    <ErrorMessage v-if="errorMessage" :message="errorMessage" />
    <p v-if="successMessage" class="success">{{ successMessage }}</p>
  </div>
</template>

<script>
import axios from 'axios';
import ErrorMessage from './ErrorMessage.vue';

export default {
  data() {
    return {
      personnes: [],
      projets: [],
      selectedPersonne: null,
      selectedProjet: null,
      role: '',
      pourcentage: 50, // Valeur initiale de la barre de pourcentage
      errorMessage: null,
      successMessage: null,
    };
  },
  components: {
    ErrorMessage,
  },
  mounted() {
    this.fetchPersonnes();
    this.fetchProjets();
  },
  methods: {
    async fetchPersonnes() {
      try {
        console.log("📡 Fetching personnes...");
        const response = await axios.get('http://localhost:8989/api/personnes');
        this.personnes = response.data;
        console.log("✅ Personnes chargées :", this.personnes);
      } catch (error) {
        console.error("❌ Erreur fetchPersonnes:", error);
        this.errorMessage = `Erreur chargement personnes: ${error.message}`;
      }
    },
    async fetchProjets() {
      try {
        console.log("📡 Fetching projets...");
        const response = await axios.get('http://localhost:8989/api/projets');
        this.projets = response.data;
        console.log("✅ Projets chargés :", this.projets);
      } catch (error) {
        console.error("❌ Erreur fetchProjets:", error);
        this.errorMessage = `Erreur chargement projets: ${error.message}`;
      }
    }
    ,
    updatePourcentage() {
      // Cette méthode est déclenchée à chaque déplacement du curseur
      console.log(`Valeur du pourcentage : ${this.pourcentage}`);
    },
    async submitForm() {
      try {
        const response = await axios.post('/api/gestion/participation', {
          matricule: this.selectedPersonne,
          codeProjet: this.selectedProjet,
          role: this.role,
          pourcentage: this.pourcentage / 100, // On convertit en valeur décimale
        });
        this.successMessage = 'Participation enregistrée avec succès';
        this.errorMessage = null; // Réinitialiser l'erreur
      } catch (error) {
        if (error.response) {
          this.errorMessage = error.response.data.message || 'Erreur inconnue';
        } else {
          this.errorMessage = 'Erreur de communication avec le serveur';
        }
        this.successMessage = null; // Réinitialiser la réussite
      }
    },
    getRangeBackground() {
      // Calcule la couleur du fond en fonction du pourcentage
      const percentage = this.pourcentage;
      return `linear-gradient(to right, blue ${percentage}%, #ddd ${percentage}%)`;
    }
  },
};
</script>

<style scoped>
.form-container {
  display: flex;
  flex-direction: column;
  gap: 20px; /* Espacement entre les éléments du formulaire */
  align-items: center;
  justify-content: center;
  width: 80%;
  margin: auto;
  padding: 20px; /* Ajout de padding */
  border: 2px solid #ccc; /* Bordure autour du formulaire */
  border-radius: 10px; /* Coins arrondis */
  background-color: #f9f9f9; /* Légère couleur de fond */
}

.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 15px;
  width: 100%;
}

.form-group label {
  font-size: 16px;
  margin-bottom: 5px;
}

.form-group select,
.form-group input {
  padding: 10px;
  font-size: 16px;
  width: 100%; /* 100% pour utiliser toute la largeur disponible */
  border: 1px solid #ccc;
  border-radius: 5px;
}

.range-labels {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  margin-top: 5px;
}

input[type="range"] {
  -webkit-appearance: none;
  width: 100%;
  height: 10px;
  background: #ddd;
  border-radius: 5px;
  outline: none;
  transition: background 0.3s ease;
}

input[type="range"]::-webkit-slider-thumb {
  -webkit-appearance: none;
  height: 20px;
  width: 20px;
  border-radius: 50%;
  background: blue;
  cursor: pointer;
}

input[type="range"]:focus {
  background: #bbb;
}

.range-value {
  font-size: 18px;
  font-weight: bold;
  margin-top: 5px;
}

.button-container {
  display: flex;
  justify-content: center;
}

button {
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
  background-color: blue;
  color: white;
  border: none;
  border-radius: 5px;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: white;
  color: blue;
}

.success {
  color: green;
}

.error {
  color: red;
}
</style>
