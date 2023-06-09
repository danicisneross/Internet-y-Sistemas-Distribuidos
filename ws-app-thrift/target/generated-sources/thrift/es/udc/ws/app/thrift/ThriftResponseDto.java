/**
 * Autogenerated by Thrift Compiler (0.16.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package es.udc.ws.app.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.16.0)", date = "2023-01-29")
public class ThriftResponseDto implements org.apache.thrift.TBase<ThriftResponseDto, ThriftResponseDto._Fields>, java.io.Serializable, Cloneable, Comparable<ThriftResponseDto> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ThriftResponseDto");

  private static final org.apache.thrift.protocol.TField RESPONSE_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("responseId", org.apache.thrift.protocol.TType.I64, (short)1);
  private static final org.apache.thrift.protocol.TField EVENT_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("eventId", org.apache.thrift.protocol.TType.I64, (short)2);
  private static final org.apache.thrift.protocol.TField USER_EMAIL_FIELD_DESC = new org.apache.thrift.protocol.TField("userEmail", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField ASSISTANCE_FIELD_DESC = new org.apache.thrift.protocol.TField("assistance", org.apache.thrift.protocol.TType.BOOL, (short)4);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new ThriftResponseDtoStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new ThriftResponseDtoTupleSchemeFactory();

  public long responseId; // required
  public long eventId; // required
  public @org.apache.thrift.annotation.Nullable java.lang.String userEmail; // required
  public boolean assistance; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    RESPONSE_ID((short)1, "responseId"),
    EVENT_ID((short)2, "eventId"),
    USER_EMAIL((short)3, "userEmail"),
    ASSISTANCE((short)4, "assistance");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // RESPONSE_ID
          return RESPONSE_ID;
        case 2: // EVENT_ID
          return EVENT_ID;
        case 3: // USER_EMAIL
          return USER_EMAIL;
        case 4: // ASSISTANCE
          return ASSISTANCE;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __RESPONSEID_ISSET_ID = 0;
  private static final int __EVENTID_ISSET_ID = 1;
  private static final int __ASSISTANCE_ISSET_ID = 2;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.RESPONSE_ID, new org.apache.thrift.meta_data.FieldMetaData("responseId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.EVENT_ID, new org.apache.thrift.meta_data.FieldMetaData("eventId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.USER_EMAIL, new org.apache.thrift.meta_data.FieldMetaData("userEmail", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.ASSISTANCE, new org.apache.thrift.meta_data.FieldMetaData("assistance", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ThriftResponseDto.class, metaDataMap);
  }

  public ThriftResponseDto() {
  }

  public ThriftResponseDto(
    long responseId,
    long eventId,
    java.lang.String userEmail,
    boolean assistance)
  {
    this();
    this.responseId = responseId;
    setResponseIdIsSet(true);
    this.eventId = eventId;
    setEventIdIsSet(true);
    this.userEmail = userEmail;
    this.assistance = assistance;
    setAssistanceIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ThriftResponseDto(ThriftResponseDto other) {
    __isset_bitfield = other.__isset_bitfield;
    this.responseId = other.responseId;
    this.eventId = other.eventId;
    if (other.isSetUserEmail()) {
      this.userEmail = other.userEmail;
    }
    this.assistance = other.assistance;
  }

  public ThriftResponseDto deepCopy() {
    return new ThriftResponseDto(this);
  }

  @Override
  public void clear() {
    setResponseIdIsSet(false);
    this.responseId = 0;
    setEventIdIsSet(false);
    this.eventId = 0;
    this.userEmail = null;
    setAssistanceIsSet(false);
    this.assistance = false;
  }

  public long getResponseId() {
    return this.responseId;
  }

  public ThriftResponseDto setResponseId(long responseId) {
    this.responseId = responseId;
    setResponseIdIsSet(true);
    return this;
  }

  public void unsetResponseId() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __RESPONSEID_ISSET_ID);
  }

  /** Returns true if field responseId is set (has been assigned a value) and false otherwise */
  public boolean isSetResponseId() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __RESPONSEID_ISSET_ID);
  }

  public void setResponseIdIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __RESPONSEID_ISSET_ID, value);
  }

  public long getEventId() {
    return this.eventId;
  }

  public ThriftResponseDto setEventId(long eventId) {
    this.eventId = eventId;
    setEventIdIsSet(true);
    return this;
  }

  public void unsetEventId() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __EVENTID_ISSET_ID);
  }

  /** Returns true if field eventId is set (has been assigned a value) and false otherwise */
  public boolean isSetEventId() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __EVENTID_ISSET_ID);
  }

  public void setEventIdIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __EVENTID_ISSET_ID, value);
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getUserEmail() {
    return this.userEmail;
  }

  public ThriftResponseDto setUserEmail(@org.apache.thrift.annotation.Nullable java.lang.String userEmail) {
    this.userEmail = userEmail;
    return this;
  }

  public void unsetUserEmail() {
    this.userEmail = null;
  }

  /** Returns true if field userEmail is set (has been assigned a value) and false otherwise */
  public boolean isSetUserEmail() {
    return this.userEmail != null;
  }

  public void setUserEmailIsSet(boolean value) {
    if (!value) {
      this.userEmail = null;
    }
  }

  public boolean isAssistance() {
    return this.assistance;
  }

  public ThriftResponseDto setAssistance(boolean assistance) {
    this.assistance = assistance;
    setAssistanceIsSet(true);
    return this;
  }

  public void unsetAssistance() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __ASSISTANCE_ISSET_ID);
  }

  /** Returns true if field assistance is set (has been assigned a value) and false otherwise */
  public boolean isSetAssistance() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __ASSISTANCE_ISSET_ID);
  }

  public void setAssistanceIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __ASSISTANCE_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case RESPONSE_ID:
      if (value == null) {
        unsetResponseId();
      } else {
        setResponseId((java.lang.Long)value);
      }
      break;

    case EVENT_ID:
      if (value == null) {
        unsetEventId();
      } else {
        setEventId((java.lang.Long)value);
      }
      break;

    case USER_EMAIL:
      if (value == null) {
        unsetUserEmail();
      } else {
        setUserEmail((java.lang.String)value);
      }
      break;

    case ASSISTANCE:
      if (value == null) {
        unsetAssistance();
      } else {
        setAssistance((java.lang.Boolean)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case RESPONSE_ID:
      return getResponseId();

    case EVENT_ID:
      return getEventId();

    case USER_EMAIL:
      return getUserEmail();

    case ASSISTANCE:
      return isAssistance();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case RESPONSE_ID:
      return isSetResponseId();
    case EVENT_ID:
      return isSetEventId();
    case USER_EMAIL:
      return isSetUserEmail();
    case ASSISTANCE:
      return isSetAssistance();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof ThriftResponseDto)
      return this.equals((ThriftResponseDto)that);
    return false;
  }

  public boolean equals(ThriftResponseDto that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_responseId = true;
    boolean that_present_responseId = true;
    if (this_present_responseId || that_present_responseId) {
      if (!(this_present_responseId && that_present_responseId))
        return false;
      if (this.responseId != that.responseId)
        return false;
    }

    boolean this_present_eventId = true;
    boolean that_present_eventId = true;
    if (this_present_eventId || that_present_eventId) {
      if (!(this_present_eventId && that_present_eventId))
        return false;
      if (this.eventId != that.eventId)
        return false;
    }

    boolean this_present_userEmail = true && this.isSetUserEmail();
    boolean that_present_userEmail = true && that.isSetUserEmail();
    if (this_present_userEmail || that_present_userEmail) {
      if (!(this_present_userEmail && that_present_userEmail))
        return false;
      if (!this.userEmail.equals(that.userEmail))
        return false;
    }

    boolean this_present_assistance = true;
    boolean that_present_assistance = true;
    if (this_present_assistance || that_present_assistance) {
      if (!(this_present_assistance && that_present_assistance))
        return false;
      if (this.assistance != that.assistance)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(responseId);

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(eventId);

    hashCode = hashCode * 8191 + ((isSetUserEmail()) ? 131071 : 524287);
    if (isSetUserEmail())
      hashCode = hashCode * 8191 + userEmail.hashCode();

    hashCode = hashCode * 8191 + ((assistance) ? 131071 : 524287);

    return hashCode;
  }

  @Override
  public int compareTo(ThriftResponseDto other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetResponseId(), other.isSetResponseId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetResponseId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.responseId, other.responseId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetEventId(), other.isSetEventId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEventId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.eventId, other.eventId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetUserEmail(), other.isSetUserEmail());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUserEmail()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.userEmail, other.userEmail);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetAssistance(), other.isSetAssistance());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetAssistance()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.assistance, other.assistance);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  @org.apache.thrift.annotation.Nullable
  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("ThriftResponseDto(");
    boolean first = true;

    sb.append("responseId:");
    sb.append(this.responseId);
    first = false;
    if (!first) sb.append(", ");
    sb.append("eventId:");
    sb.append(this.eventId);
    first = false;
    if (!first) sb.append(", ");
    sb.append("userEmail:");
    if (this.userEmail == null) {
      sb.append("null");
    } else {
      sb.append(this.userEmail);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("assistance:");
    sb.append(this.assistance);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class ThriftResponseDtoStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public ThriftResponseDtoStandardScheme getScheme() {
      return new ThriftResponseDtoStandardScheme();
    }
  }

  private static class ThriftResponseDtoStandardScheme extends org.apache.thrift.scheme.StandardScheme<ThriftResponseDto> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, ThriftResponseDto struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // RESPONSE_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.responseId = iprot.readI64();
              struct.setResponseIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // EVENT_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.eventId = iprot.readI64();
              struct.setEventIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // USER_EMAIL
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.userEmail = iprot.readString();
              struct.setUserEmailIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // ASSISTANCE
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.assistance = iprot.readBool();
              struct.setAssistanceIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, ThriftResponseDto struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(RESPONSE_ID_FIELD_DESC);
      oprot.writeI64(struct.responseId);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(EVENT_ID_FIELD_DESC);
      oprot.writeI64(struct.eventId);
      oprot.writeFieldEnd();
      if (struct.userEmail != null) {
        oprot.writeFieldBegin(USER_EMAIL_FIELD_DESC);
        oprot.writeString(struct.userEmail);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(ASSISTANCE_FIELD_DESC);
      oprot.writeBool(struct.assistance);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ThriftResponseDtoTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public ThriftResponseDtoTupleScheme getScheme() {
      return new ThriftResponseDtoTupleScheme();
    }
  }

  private static class ThriftResponseDtoTupleScheme extends org.apache.thrift.scheme.TupleScheme<ThriftResponseDto> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ThriftResponseDto struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetResponseId()) {
        optionals.set(0);
      }
      if (struct.isSetEventId()) {
        optionals.set(1);
      }
      if (struct.isSetUserEmail()) {
        optionals.set(2);
      }
      if (struct.isSetAssistance()) {
        optionals.set(3);
      }
      oprot.writeBitSet(optionals, 4);
      if (struct.isSetResponseId()) {
        oprot.writeI64(struct.responseId);
      }
      if (struct.isSetEventId()) {
        oprot.writeI64(struct.eventId);
      }
      if (struct.isSetUserEmail()) {
        oprot.writeString(struct.userEmail);
      }
      if (struct.isSetAssistance()) {
        oprot.writeBool(struct.assistance);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, ThriftResponseDto struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(4);
      if (incoming.get(0)) {
        struct.responseId = iprot.readI64();
        struct.setResponseIdIsSet(true);
      }
      if (incoming.get(1)) {
        struct.eventId = iprot.readI64();
        struct.setEventIdIsSet(true);
      }
      if (incoming.get(2)) {
        struct.userEmail = iprot.readString();
        struct.setUserEmailIsSet(true);
      }
      if (incoming.get(3)) {
        struct.assistance = iprot.readBool();
        struct.setAssistanceIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

