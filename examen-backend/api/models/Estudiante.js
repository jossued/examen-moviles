/**
 * Estudiante.js
 *
 * @description :: A model definition represents a database table/collection.
 * @docs        :: https://sailsjs.com/docs/concepts/models-and-orm/models
 */

module.exports = {

  attributes: {

    nombres: {
      type: 'string'
    },
    apellidos: {
      type: 'string'
    },
    fechaNacimiento:{
      type: 'string',
      columnType: 'date'
    }

  },

};

