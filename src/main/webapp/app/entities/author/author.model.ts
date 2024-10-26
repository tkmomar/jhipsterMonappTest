export interface IAuthor {
  id: number;
  lastname?: string | null;
  firstname?: string | null;
  style?: string | null;
}

export type NewAuthor = Omit<IAuthor, 'id'> & { id: null };
