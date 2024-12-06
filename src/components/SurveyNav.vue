<template>
  <div>
    <nav class="survey-nav">
      <div class="logo">
        <a href="#">SurveyApp</a>
      </div>
      <div class="actions">

        <button class="view-button" @click="fetchSurveys">View Surveys</button>
      </div>
    </nav>

    <div class="survey-results" v-if="surveys.length">
      <h2>Submitted Surveys</h2>
      <ul>
        <li v-for="(survey, index) in surveys" :key="index">
          {{ survey.name }} - {{ survey.email }} - {{ survey.feedback }}
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "SurveyNav",
  data() {
    return {
      surveys: [], // Store surveys fetched from the backend
    };
  },
  methods: {
    async fetchSurveys() {
      try {
        const response = await axios.get("http://<BACKEND_API_URL>/api/surveys");
        this.surveys = response.data; // Assuming the backend returns an array of surveys
      } catch (error) {
        console.error("Error fetching surveys:", error);
        alert("Failed to fetch surveys. Please try again.");
      }
    },
  },
};
</script>

<style scoped>
.survey-nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 2rem;
  background-color: #f8f9fa;
  box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1);
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
}

.logo a {
  font-size: 1.5rem;
  font-weight: bold;
  text-decoration: none;
  color: #007aff;
}

.actions {
  display: flex;
  gap: 1rem;
}

.cta-button,
.view-button {
  padding: 0.5rem 1rem;
  font-size: 1rem;
  color: #fff;
  background-color: #007aff;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s;
}

.cta-button:hover,
.view-button:hover {
  background-color: #005bb5;
  transform: scale(1.05);
}

.cta-button:active,
.view-button:active {
  background-color: #003e82;
}

.survey-results {
  margin: 2rem auto;
  max-width: 600px;
  padding: 1rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.survey-results h2 {
  text-align: center;
  margin-bottom: 1rem;
  color: #007aff;
}

.survey-results ul {
  list-style: none;
  padding: 0;
}

.survey-results li {
  padding: 0.5rem 0;
  border-bottom: 1px solid #ddd;
  font-size: 1rem;
}

.survey-results li:last-child {
  border-bottom: none;
}
</style>
