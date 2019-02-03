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
    },
    semestreActual:{
      type: 'number',
      columnType: 'int'
    },
    graduado:{
      type: 'boolean',
    },
    materias:{
      collection: 'Materia',
      via: 'idEstudiante'
    }


  },

};

