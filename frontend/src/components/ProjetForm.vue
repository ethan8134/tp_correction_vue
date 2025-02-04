<template>
  <div class="container">
    <h2>Ajouter un projet</h2>
    <!-- Un formulaire pour saisir les valeurs du projet à ajouter -->
    <form @submit.prevent="ajouteProjet">
      <div>
        <div>
          <label for="nom">Nom : </label>
          <input id="nom" v-model="data.formulaire.nom" size="25" maxlength="25">
          <button type="submit">Ajouter</button>
        </div>
      </div>
    </form>
    <div>
      <!-- Un tableau pour afficher la liste des pays -->
      <table>
        <thead>
        <tr>
          <th>id</th>
          <th>Nom</th>
          <th>Debut</th>
          <th>Fin</th>
        </tr>
        </thead>
        <tbody>
        <!-- Une ligne pour chaque pays -->
        <tr v-for="projet in data.projets" :key="projet.id">
          <td>{{ projet.id }}</td>
          <td>{{ projet.nom }}</td>
          <td>{{ projet.debut }}</td>
          <td>{{ projet.fin }}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import {onMounted, reactive} from "vue";
// Importer la fonction doAjaxRequest qui gère les erreurs d'API
import doAjaxRequest from "@/util/util.js"

// Pour réinitialiser le formulaire
const projetVide = {
  nom: ""
};

// Les données du composant
let data = reactive({
  // Les données saisies dans le formulaire
  formulaire: {...projetVide},
  // Les projets récupérés depuis l'API
  projets: [],
});

function ajouteProjet() {
  // Ajouter un projet avec les données du formulaire
  const options = { // Options de la requête fetch
    method: "POST", // Verbe HTTP POST pour ajouter un enregistrement
    // On transmet les données du formulaire dans le corps de la requête
    body: JSON.stringify(data.formulaire),
    headers: {
      "Content-Type": "application/json",
    },
  };
  // On appelle l'API REST générée par les repositories Spring Data REST
  doAjaxRequest("/api/projets", options)
    .then((result) => {
      console.log("Projet ajouté :", result);
      // Réinitialiser le formulaire
      data.formulaire = {...projetVide};
      refresh(); // Rafraîchir la liste des pays
    })
    .catch(error => alert(error.message));
}

function refresh() {
  doAjaxRequest("/api/projets") // Méthode GET par défaut
    .then((result) => {
      data.projets = result._embedded.projets;
    })
    .catch(error => alert(error.message));
}

// Appeler la fonction refresh() pour récupérer la liste des pays au chargement du composant
onMounted(refresh);
</script>

<!-- Un CSS pour ce composant -->
<style scoped>
.container {
  margin: 2rem auto;
  max-width: 600px;
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
}

.form-group {
  margin-bottom: 15px;
}

.label {
  display: block;
  font-weight: bold;
  margin-bottom: 5px;
}

.input-field {
  width: 100%;
  padding: 10px;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.input-field:focus {
  outline: none;
  border-color: #007BFF;
  box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
}

.button-container {
  text-align: center;
}

.submit-button {
  padding: 10px 20px;
  font-size: 1rem;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.submit-button:hover {
  background-color: #0056b3;
}

.table-container {
  margin-top: 20px;
}

table {
  width: 100%;
  border-collapse: collapse; /* Supprime les bordures en double */
  margin-top: 10px;
}

thead th {
  background-color: #007BFF;
  color: #fff;
  padding: 10px;
  text-align: left; /* Alignement à gauche */
  border: 1px solid #ddd;
}

tbody td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left; /* Alignement à gauche */
}

tbody tr:nth-child(odd) {
  background-color: #f2f2f2; /* Lignes alternées pour une meilleure lisibilité */
}

tbody tr:hover {
  background-color: #eaeaea;
}

th, td {
  font-size: 0.9rem;
  text-align: left;
}

code {
  color: #d63384;
  font-weight: bold;
  background-color: #f8f9fa;
  padding: 2px 4px;
  border-radius: 3px;
}

</style>
