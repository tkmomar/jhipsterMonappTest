import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'monAppTestApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'author',
    data: { pageTitle: 'monAppTestApp.author.home.title' },
    loadChildren: () => import('./author/author.routes'),
  },
  {
    path: 'category',
    data: { pageTitle: 'monAppTestApp.category.home.title' },
    loadChildren: () => import('./category/category.routes'),
  },
  {
    path: 'post',
    data: { pageTitle: 'monAppTestApp.post.home.title' },
    loadChildren: () => import('./post/post.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
