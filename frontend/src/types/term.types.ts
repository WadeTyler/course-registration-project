
// Term fields
export type Term = {
  id: number;
  startDate: string;
  endDate: string;
  registrationStart: string;
  registrationEnd: string;
  createdAt: string;
}

// Term fields that can be modified and are required when updating
export type ManageTermRequest = {
  startDate: string;
  endDate: string;
  registrationStart: string;
  registrationEnd: string;
}