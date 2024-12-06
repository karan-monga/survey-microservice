import { createRouter, createWebHistory } from 'vue-router';
import SurveyForm from '../components/SurveyForm.vue';

const routes = [
  { path: '/', component: SurveyForm, name: 'SurveyForm' },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
