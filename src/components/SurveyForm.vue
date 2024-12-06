<template>
  <div class="survey-form-container">
    <h1></h1>
    <div class="content-wrapper">
      <form @submit.prevent="handleSubmit" class="survey-form">
        <div class="form-group">
          <label for="firstName">First Name</label>
          <input v-model="form.firstName" type="text" id="firstName" required />
        </div>
        <div class="form-group">
          <label for="lastName">Last Name</label>
          <input v-model="form.lastName" type="text" id="lastName" required />
        </div>
        <div class="form-group">
          <label for="address">Address</label>
          <input v-model="form.address" type="text" id="address" required />
        </div>
        <div class="form-group">
          <label for="city">City</label>
          <input v-model="form.city" type="text" id="city" required />
        </div>
        <div class="form-group">
          <label for="state">State</label>
          <input v-model="form.state" type="text" id="state" required />
        </div>
        <div class="form-group">
          <label for="zip">ZIP Code</label>
          <input v-model="form.zip" type="text" id="zip" required />
        </div>
        <div class="form-group">
          <label for="telephone">Telephone</label>
          <input v-model="form.telephone" type="text" id="telephone" required />
        </div>
        <div class="form-group">
          <label for="email">Email</label>
          <input v-model="form.email" type="email" id="email" required />
        </div>
        <div class="form-group">
          <label for="dateOfSurvey">Date of Survey</label>
          <input v-model="form.dateOfSurvey" type="date" id="dateOfSurvey" required />
        </div>
        <div class="form-group">
          <label for="likedMost">What did you like most?</label>
          <select v-model="form.likedMost" id="likedMost">
            <option value="campus">Campus</option>
            <option value="faculty">Faculty</option>
            <option value="sports">Sports</option>
            <option value="other">Other</option>
          </select>
        </div>
        <div class="form-group">
          <label for="interestedBy">How did you hear about us?</label>
          <select v-model="form.interestedBy" id="interestedBy">
            <option value="internet">Internet</option>
            <option value="newspaper">Newspaper</option>
            <option value="friend">Friend</option>
            <option value="other">Other</option>
          </select>
        </div>
        <div class="form-group">
          <label for="recommendationLikelihood">How likely are you to recommend us?</label>
          <select v-model="form.recommendationLikelihood" id="recommendationLikelihood">
            <option value="Very Likely">Very Likely</option>
            <option value="Somewhat Likely">Somewhat Likely</option>
            <option value="Not Likely">Not Likely</option>
          </select>
        </div>
        <button type="submit" class="btn">Submit Survey</button>
        <button type="button" @click="clearForm" class="btn secondary">Clear Form</button>
      </form>

      <div class="survey-table-container">
        <h2>Submitted Surveys</h2>
        <table class="survey-table">
          <thead>
            <tr>
              <th>First Name</th>
              <th>Last Name</th>
              <th>Address</th>
              <th>City</th>
              <th>State</th>
              <th>Zip Code</th>
              <th>Telephone</th>
              <th>Email</th>
              <th>Date of Survey</th>
              <th>What did you like most?</th>
              <th>How did you hear about us?</th>
              <th>How likely are you to recommend us?</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(survey) in surveys" :key="survey.id">
              <td>{{ survey.firstName }}</td>
              <td>{{ survey.lastName }}</td>
              <td>{{ survey.address }}</td>
              <td>{{ survey.city }}</td>
              <td>{{ survey.state }}</td>
              <td>{{ survey.zip }}</td>
              <td>{{ survey.telephone }}</td>
              <td>{{ survey.email }}</td>
              <td>{{ survey.dateOfSurvey }}</td>
              <td>{{ survey.likedMost }}</td>
              <td>{{ survey.interestedBy }}</td>
              <td>{{ survey.recommendationLikelihood }}</td>
              <td>
                <button @click="editSurvey(survey)" class="btn action-btn">Edit</button>
                <button @click="deleteSurvey(survey.id)" class="btn action-btn danger">Delete</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      form: {
        firstName: "",
        lastName: "",
        address: "",
        city: "",
        state: "",
        zip: "",
        telephone: "",
        email: "",
        dateOfSurvey: "",
        likedMost: "",
        interestedBy: "",
        recommendationLikelihood: "",
      },
      surveys: [],
      editingSurveyId: null, // Track the survey being edited
    };
  },
  methods: {
    async handleSubmit() {
      try {
        if (this.editingSurveyId) {
          // Update existing survey
          await axios.put(
            `http://localhost:8080/api/surveys/${this.editingSurveyId}`,
            this.form
          );
          alert("Survey updated successfully!");
        } else {
          // Create new survey
          await axios.post("http://localhost:8080/api/surveys", this.form);
          alert("Survey submitted successfully!");
        }
        this.fetchSurveys();
        this.clearForm();
      } catch (error) {
        console.error("Error submitting survey:", error);
      }
    },
    async fetchSurveys() {
      try {
        const response = await axios.get("http://localhost:8080/api/surveys");
        this.surveys = response.data;
      } catch (error) {
        console.error("Error fetching surveys:", error);
      }
    },
    async deleteSurvey(id) {
      try {
        await axios.delete(`http://localhost:8080/api/surveys/${id}`);
        alert("Survey deleted successfully!");
        this.fetchSurveys();
      } catch (error) {
        console.error("Error deleting survey:", error);
      }
    },
    editSurvey(survey) {
      this.editingSurveyId = survey.id;
      this.form = { ...survey };
    },
    clearForm() {
      this.editingSurveyId = null;
      this.form = {
        firstName: "",
        lastName: "",
        address: "",
        city: "",
        state: "",
        zip: "",
        telephone: "",
        email: "",
        dateOfSurvey: "",
        likedMost: "",
        interestedBy: "",
        recommendationLikelihood: "",
      };
    },
  },
  mounted() {
    this.fetchSurveys();
  },
};
</script>

<style scoped>
.survey-form-container {
  max-width: 1800px;
  margin: 0 auto;
  font-family: "San Francisco", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
}

h1,
h2 {
  text-align: center;
  color: #333;
}

h1{
padding-top:10px;
}

.content-wrapper {
  display: flex;
  gap: 10px;
}

.survey-form {
  flex: 0 0 20%;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 5px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

label {
  margin-bottom: 0.5rem;
  font-weight: bold;
}

input,
select {
  padding: 0.5rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 5px;
}

button {
  padding: 0.75rem 1.5rem;
  font-size: 1rem;
  color: #fff;
  background-color: #0078d4;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button.secondary {
  background-color: #555;
}

button:hover {
  background-color: #005a9e;
}

button.secondary:hover {
  background-color: #333;
}

.survey-table-container {
  flex: 0 0 80%;
  overflow-x: auto;
}

.survey-table {
  width: 100%;
  border-collapse: collapse;
}

.survey-table th,
.survey-table td {
  border: 1px solid #ddd;
  padding: 0.75rem;
  text-align: left;
}

.survey-table th {
  background-color: #f4f4f4;
  font-weight: bold;
}

.survey-table tr:nth-child(even) {
  background-color: #f9f9f9;
}

.action-btn {
  margin: 0 5px;
}

.danger {
  background-color: #d9534f;
}

.danger:hover {
  background-color: #c9302c;
}
</style>
