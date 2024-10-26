import dayjs from 'dayjs/esm';

import { IPost, NewPost } from './post.model';

export const sampleWithRequiredData: IPost = {
  id: 9277,
  title: 'badaboum ci',
};

export const sampleWithPartialData: IPost = {
  id: 22496,
  title: 'comme',
  dateCreation: dayjs('2024-10-25T03:45'),
};

export const sampleWithFullData: IPost = {
  id: 14729,
  title: 'cuicui triathlète géométrique',
  content: 'étant donné que équipe de recherche équipe de recherche',
  dateCreation: dayjs('2024-10-26T00:11'),
};

export const sampleWithNewData: NewPost = {
  title: 'dring',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
