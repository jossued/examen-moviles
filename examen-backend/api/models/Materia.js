/**
 * Materia.js
 *
 * @description :: A model definition represents a database table/collection.
 * @docs        :: https://sailsjs.com/docs/concepts/models-and-orm/models
 */

module.exports = {

  attributes: {

    codigoBarra:{
      type: 'number',
      columnType: 'int'
    },
    nombre: {
      type: 'string'
    },
    codigo: {
      type: 'number',
      columnType: 'int'
    },
    descripcion: {
      type: 'string'
    },
    activo: {
      type: 'boolean'
    },
    fechaCreacion: {
      type: 'string',
      columnType: 'date'
    },
    numeroHorasPorSemana: {
      type: 'number',
      columnType: 'int'
    },
    idEstudiante: {
      model: 'Estudiante'
    }

  },

};

