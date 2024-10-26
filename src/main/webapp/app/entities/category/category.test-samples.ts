import { ICategory, NewCategory } from './category.model';

export const sampleWithRequiredData: ICategory = {
  id: 2532,
  name: 'responsable faire',
};

export const sampleWithPartialData: ICategory = {
  id: 25980,
  name: "clientèle aujourd'hui au point que",
};

export const sampleWithFullData: ICategory = {
  id: 8154,
  name: 'parmi tenir',
};

export const sampleWithNewData: NewCategory = {
  name: 'altruiste',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
