/**
 * Faire un appel AJAX avec l'API fetch
 * Permet de récupérer erreur réseau et erreur de l'API
 * usage :
 * doAjaxRequest(url, options).then(showResult).catch(showError);
 * @param {string} url L'URL de l'API
 * @param {object} options Les options de la requête AJAX
 * @returns {Promise} Une promesse qui sera résolue avec le résultat de l'appel AJAX
 * @throws {object} Une exception qui sera levée si l'API a signalé une erreur
 */
export default function doAjaxRequest(url, options = {}) {
  return fetch(`http://localhost:8989${url}`, options)
    .then(response => {
      if (!response.ok) {
        throw new Error(`Erreur HTTP! Statut : ${response.status}`);
      }
      return response.json();
    });
}

