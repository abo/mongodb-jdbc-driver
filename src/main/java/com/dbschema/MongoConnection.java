package com.dbschema;

import com.dbschema.wrappers.WrappedMongoClient;
import com.dbschema.wrappers.WrappedMongoDatabase;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * Copyright Wise Coders GmbH. Free to use. Changes allowed only as push requests into https://bitbucket.org/dbschema/mongodb-jdbc-driver.
 */
public class MongoConnection implements Connection
{
    private String catalog;
	final WrappedMongoClient client;
	private boolean isClosed = false;
	private boolean isReadOnly = false;


	MongoConnection(WrappedMongoClient client) throws SQLException {
		this.client = client;
        setCatalog( client.getCurrentDatabaseName() );
        switch ( client.pingServer() ){
			case FAILED : throw new SQLException("Connection to server failed.");
			case TIMEOUT : throw new SQLException("Timeout connecting the server.");
		}
	}

	public String getCatalog(){
        return catalog;
    }

    @Override
	public <T> T unwrap(Class<T> iface) throws SQLException	{
		checkClosed();
		return null;
	}

    @Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException	{
		checkClosed();
		return false;
	}

	/**
	 * @see java.sql.Connection#createStatement()
	 */
    @Override
	public Statement createStatement() throws SQLException {
		checkClosed();
		return new MongoPreparedStatement(this);
	}

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException{
        checkClosed();
        return new MongoPreparedStatement(this);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability ) throws SQLException {
        checkClosed();
        return new MongoPreparedStatement(this);
    }


    @Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		checkClosed();
        return new MongoPreparedStatement(this, sql );
	}

    @Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		checkClosed();
		return null;
	}

	/**
	 * @see java.sql.Connection#nativeSQL(java.lang.String)
	 */
    @Override
	public String nativeSQL(String sql) throws SQLException	{
		checkClosed();
		throw new UnsupportedOperationException("MongoDB does not support SQL natively.");
	}

	/**
	 * @see java.sql.Connection#setAutoCommit(boolean)
	 */
    @Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		checkClosed();
	}

	/**
	 * @see java.sql.Connection#getAutoCommit()
	 */
    @Override
	public boolean getAutoCommit() throws SQLException {
		checkClosed();
		return true;
	}

    @Override
	public void commit() throws SQLException {
		checkClosed();
	}

    @Override
	public void rollback() throws SQLException {
		checkClosed();
	}

    @Override
	public void close() {
    	client.close();
		isClosed = true;
	}

    @Override
	public boolean isClosed() {
		return isClosed;
	}

    private final MongoDatabaseMetaData metaData = new MongoDatabaseMetaData(this);

    @Override
	public DatabaseMetaData getMetaData() throws SQLException {
		checkClosed();
        return metaData;
	}

    @Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		checkClosed();
		isReadOnly = readOnly;
	}

	/**
	 * @see java.sql.Connection#isReadOnly()
	 */
    @Override
	public boolean isReadOnly() throws SQLException {
		checkClosed();
		return isReadOnly;
	}

    @Override
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

    @Override
	public void setTransactionIsolation(int level) throws SQLException	{
		checkClosed();
		// Since the only valid value for MongDB is Connection.TRANSACTION_NONE, and the javadoc for this method
		// indicates that this is not a valid value for level here, throw unsupported operation exception.
		throw new UnsupportedOperationException("MongoDB provides no support for transactions.");
	}

	/**
	 * @see java.sql.Connection#getTransactionIsolation()
	 */
    @Override
	public int getTransactionIsolation() throws SQLException {
		checkClosed();
		return Connection.TRANSACTION_NONE;
	}

    @Override
	public SQLWarning getWarnings() throws SQLException	{
		checkClosed();
		return null;
	}

    @Override
	public void clearWarnings() throws SQLException {
		checkClosed();
	}


    @Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
		throws SQLException	{
		return null;
	}

    @Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
		throws SQLException {
		return null;
	}

    @Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return null;
	}

    @Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException{}

    @Override
	public void setHoldability(int holdability) throws SQLException{}

    @Override
	public int getHoldability() throws SQLException {
		return 0;
	}

    @Override
	public Savepoint setSavepoint() throws SQLException	{
		return null;
	}

    @Override
	public Savepoint setSavepoint(String name) throws SQLException
	{
		return null;
	}

    @Override
	public void rollback(Savepoint savepoint) throws SQLException {
	}

    @Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException{}


    @Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
		int resultSetHoldability) throws SQLException {
		return null;
	}

    @Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
		int resultSetHoldability) throws SQLException {
		return null;
	}

    @Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		return null;
	}

    @Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		return null;
	}

    @Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException	{
		return null;
	}

    @Override
	public Clob createClob() throws SQLException {
		return null;
	}

    @Override
	public Blob createBlob() throws SQLException {
		return null;
	}

    @Override
	public NClob createNClob() throws SQLException
	{
		checkClosed();
		return null;
	}

    @Override
	public SQLXML createSQLXML() throws SQLException {
		checkClosed();
		return null;
	}

	/**
	 * @see java.sql.Connection#isValid(int)
	 */
    @Override
	public boolean isValid(int timeout) throws SQLException
	{
		checkClosed();
		return true;
	}

	/**
	 * @see java.sql.Connection#setClientInfo(java.lang.String, java.lang.String)
	 */
    @Override
	public void setClientInfo(String name, String value) throws SQLClientInfoException
	{
		/* MongoDB does not support setting client information in the database. */
	}

	/**
	 * @see java.sql.Connection#setClientInfo(java.util.Properties)
	 */
    @Override
	public void setClientInfo(Properties properties) throws SQLClientInfoException
	{
		/* MongoDB does not support setting client information in the database. */
	}

	/**
	 * @see java.sql.Connection#getClientInfo(java.lang.String)
	 */
    @Override
	public String getClientInfo(String name) throws SQLException
	{
		checkClosed();
		return null;
	}

	/**
	 * @see java.sql.Connection#getClientInfo()
	 */
    @Override
	public Properties getClientInfo() throws SQLException
	{
		checkClosed();
		return null;
	}

    @Override
	public Array createArrayOf(String typeName, Object[] elements) throws SQLException
	{
		checkClosed();
		
		return null;
	}

	/**
	 * @see java.sql.Connection#createStruct(java.lang.String, java.lang.Object[])
	 */
    @Override
	public Struct createStruct(String typeName, Object[] attributes) throws SQLException
	{
		checkClosed();
		
		return null;
	}

	/**
	 * @return
	 */
	public String getUrl()
	{
		return client.getURI();
	}

    public List<WrappedMongoDatabase> getDatabases(){
        return client.getDatabases();
    }

    public List<String> getDatabaseNames(){
        return client.getDatabaseNames();
    }

    public WrappedMongoDatabase getDatabase(String name ){
        return client.getDatabase( name );
    }


	private void checkClosed() throws SQLException
	{
		if (isClosed)
		{
			throw new SQLException("Statement was previously closed.");
		}
	}

    @Override
    public void setSchema(String schema) throws SQLException {
    }

    @Override
    public String getSchema() throws SQLException {
        return null;  
    }

    @Override
    public void abort(Executor executor) throws SQLException {
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return 0;  
    }

}
