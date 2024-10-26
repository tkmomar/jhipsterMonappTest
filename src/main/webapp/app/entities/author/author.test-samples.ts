import { IAuthor, NewAuthor } from './author.model';

export const sampleWithRequiredData: IAuthor = {
  id: 26871,
  lastname: 'tellement de façon à ce que oups',
  firstname: 'négocier subito',
};

export const sampleWithPartialData: IAuthor = {
  id: 7608,
  lastname: 'dès',
  firstname: 'avant-hier euh de peur que',
};

export const sampleWithFullData: IAuthor = {
  id: 7318,
  lastname: 'toutefois aigre',
  firstname: 'fonctionnaire pacifique cadre',
  style: 'hors',
};

export const sampleWithNewData: NewAuthor = {
  lastname: 'que dehors tic-tac',
  firstname: 'dès que',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
